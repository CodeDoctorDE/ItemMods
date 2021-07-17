package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.itemmods.ItemMods;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author CodeDoctorDE
 */
public class PackManager {
    private final Path packPath;
    private final Path importPath;
    private final Path exportPath;
    private final Path resourcePacksPath;
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
    }

    private static void zipFile(Path fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
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

    public Path getImportPath() {
        return importPath;
    }

    public Path getExportPath() {
        return exportPath;
    }

    public Path getResourcePacksPath() {
        return resourcePacksPath;
    }

    public void reload() {
        packs.clear();
        try (Stream<Path> paths = Files.walk(packPath)) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        var pack = new ItemModsPack("");
                        try {
                            pack.load(path);
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

    public void save(String name) {
        var pack = getPack(name);
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
    }

    public List<ItemModsPack> getPacks() {
        return Collections.unmodifiableList(packs);
    }

    public List<ItemModsPack> getInactivePacks() {
        return Collections.unmodifiableList(inactivePacks);
    }

    public Set<String> getPackNames() {
        return packs.stream().map(NamedPackObject::getName).collect(Collectors.toSet());
    }

    public void registerPack(ItemModsPack pack) {
        if (!NamedPackObject.NAME_PATTERN.matcher(pack.getName()).matches()) return;
        if (packs.stream().anyMatch(current -> current.getName().equals(pack.getName())))
            return;
        packs.add(pack);
    }

    public void unregisterPack(String name) {
        packs.removeIf(itemModsPack -> itemModsPack.getName().equals(name));
    }

    public void activatePack(String name) {
        inactivePacks.stream().filter(itemModsPack -> itemModsPack.getName().equals(name)).forEach(itemModsPack -> {
            inactivePacks.remove(itemModsPack);
            packs.add(itemModsPack);
        });
    }

    public void deactivatePack(String name) {
        packs.stream().filter(itemModsPack -> itemModsPack.getName().equals(name)).forEach(itemModsPack -> {
            packs.remove(itemModsPack);
            inactivePacks.add(itemModsPack);
        });
    }

    public void deletePack(String name) {
        packs.stream().filter(itemModsPack -> itemModsPack.getName().equals(name)).forEach(itemModsPack -> {
            packs.remove(itemModsPack);
            if (itemModsPack.isEditable()) {
                try {
                    Files.deleteIfExists(Paths.get(packPath.toString(), name));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Path getPackPath() {
        return packPath;
    }

    @Nullable
    public ItemModsPack getPack(String name) {
        return packs.stream().filter(pack -> pack.getName().equals(name)).findFirst().orElse(null);
    }

    public void export(String name) throws IOException {
        var pack = getPack(name);
        if (pack == null || !pack.isEditable())
            return;
        var zipOut = new ZipOutputStream(Files.newOutputStream(Paths.get(exportPath.toString(), name)));
        var path = Paths.get(packPath.toString(), name);
        if (!Files.exists(path))
            return;
        zipFile(path, name, zipOut);
    }
}
