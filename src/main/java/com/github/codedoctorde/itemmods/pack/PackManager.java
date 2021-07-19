package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.itemmods.ItemMods;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author CodeDoctorDE
 */
public class PackManager {
    private final @NotNull Path packPath;
    private final @NotNull Path importPath;
    private final @NotNull Path exportPath;
    private final @NotNull Path resourcePacksPath;
    private final List<ItemModsPack> packs = new ArrayList<>();
    private final List<ItemModsPack> inactivePacks = new ArrayList<>();

    public PackManager() throws IOException {
        packPath = Paths.get(ItemMods.getPlugin().getDataFolder().getPath(), "packs");
        importPath = Paths.get(ItemMods.getPlugin().getDataFolder().getPath(), "imports");
        exportPath = Paths.get(ItemMods.getPlugin().getDataFolder().getPath(), "exports");
        //noinspection SpellCheckingInspection
        resourcePacksPath = Paths.get(ItemMods.getPlugin().getDataFolder().getPath(), "resourcepacks");
        try {
            Files.createDirectory(packPath);
            Files.createDirectory(importPath);
            Files.createDirectory(exportPath);
            Files.createDirectory(resourcePacksPath);
        } catch (FileAlreadyExistsException ignored) {

        }
        reload();
    }

    private static void zipFile(@NotNull Path fileToZip, @NotNull String fileName, @NotNull ZipOutputStream zipOut) throws IOException {
        if (Files.isHidden(fileToZip)) {
            return;
        }
        if (Files.isDirectory(fileToZip)) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
            }
            zipOut.closeEntry();
            try (Stream<Path> paths = Files.walk(fileToZip)) {
                paths.forEach(path -> {
                    try {
                        zipFile(path, fileName + "/" + path.getFileName(), zipOut);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            return;
        }
        var fis = Files.newInputStream(fileToZip);
        var zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public @NotNull Path getImportPath() {
        return importPath;
    }

    public @NotNull Path getExportPath() {
        return exportPath;
    }

    public @NotNull Path getResourcePacksPath() {
        return resourcePacksPath;
    }

    public void reload() {
        packs.clear();
        try (Stream<Path> paths = Files.walk(packPath)) {
            paths
                    .filter(Files::isDirectory)
                    .filter(path -> Files.exists(Paths.get(path.toString(), "pack.json")))
                    .forEach(path -> {
                        try {
                            System.out.println(path.getFileName().toString());
                            var pack = new ItemModsPack(path);
                            packs.add(pack);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        getPackNames().forEach(this::save);
    }

    public void save(String namespace) {
        var pack = getPack(namespace);
        if (pack == null || !pack.isEditable() || !NamedPackObject.NAME_PATTERN.matcher(
                pack.getName()).matches())
            return;
        var path = Paths.get(packPath.toString(), pack.getName());
        if (!Files.exists(path)) try {
            Files.createDirectories(path);
            pack.save(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pack.save(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public @NotNull List<ItemModsPack> getPacks() {
        return Collections.unmodifiableList(packs);
    }

    public @NotNull List<ItemModsPack> getInactivePacks() {
        return Collections.unmodifiableList(inactivePacks);
    }

    public Set<String> getPackNames() {
        return packs.stream().map(NamedPackObject::getName).collect(Collectors.toSet());
    }

    public void registerPack(@NotNull ItemModsPack pack) {
        if (!NamedPackObject.NAME_PATTERN.matcher(pack.getName()).matches()) return;
        if (packs.stream().anyMatch(current -> current.getName().equals(pack.getName())))
            return;
        packs.add(pack);
    }

    public void unregisterPack(String name) {
        packs.removeIf(itemModsPack -> itemModsPack.getName().equals(name));
    }

    public void activatePack(String name) {
        inactivePacks.stream().filter(itemModsPack -> itemModsPack.getName().equals(name)).collect(Collectors.toList()).forEach(itemModsPack -> {
            inactivePacks.remove(itemModsPack);
            packs.add(itemModsPack);
        });
    }

    public void deactivatePack(String name) {
        packs.stream().filter(itemModsPack -> itemModsPack.getName().equals(name)).collect(Collectors.toList()).forEach(itemModsPack -> {
            packs.remove(itemModsPack);
            inactivePacks.add(itemModsPack);
        });
    }

    public void deletePack(String name) {
        packs.stream().filter(itemModsPack -> itemModsPack.getName().equals(name)).collect(Collectors.toList()).forEach(itemModsPack -> {
            packs.remove(itemModsPack);
            if (itemModsPack.isEditable()) {
                var rootPath = Paths.get(packPath.toString(), itemModsPack.getName());
                try (Stream<Path> walk = Files.walk(rootPath)) {
                    walk.sorted(Comparator.reverseOrder())
                            .forEach((path) -> {
                                try {
                                    Files.delete(path);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public @NotNull Path getPackPath() {
        return packPath;
    }

    @Nullable
    public ItemModsPack getPack(String name) {
        return packs.stream().filter(pack -> pack.getName().equals(name)).findFirst().orElse(null);
    }

    public void export(@NotNull String name) throws IOException {
        var pack = getPack(name);
        if (pack == null || !pack.isEditable())
            return;
        var zipOut = new ZipOutputStream(Files.newOutputStream(Paths.get(exportPath.toString(), name)));
        var path = Paths.get(packPath.toString(), name);
        if (!Files.exists(path))
            return;
        zipFile(path, name, zipOut);
    }

    public boolean isActivated(String name) {
        return packs.stream().anyMatch(pack -> pack.getName().equals(name));
    }
}
