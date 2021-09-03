package dev.linwood.itemmods.pack;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private final @NotNull Path presetPath;
    private final List<ItemModsPack> packs = new ArrayList<>();
    private final List<ItemModsPack> inactivePacks = new ArrayList<>();

    public PackManager() {
        packPath = Paths.get(ItemMods.getPlugin().getDataFolder().getPath(), "packs");
        presetPath = Paths.get(ItemMods.getPlugin().getDataFolder().getPath(), "preset");
        try {
            Files.createDirectories(packPath);
            Files.createDirectories(Paths.get(presetPath.toString(), "default"));
            if (hasNoPreset())
                Bukkit.getConsoleSender().sendMessage(ItemMods.getTranslationConfig().getTranslation("plugin.no-preset"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        reload();
    }

    private static void pack(Path sourceDirPath, Path zipFilePath) throws IOException {
        Files.deleteIfExists(zipFilePath);
        Path p = Files.createFile(zipFilePath);
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Files.walk(sourceDirPath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    public boolean hasNoPreset() throws IOException {
        return !Files.newDirectoryStream(presetPath).iterator().hasNext();
    }

    public void reload() {
        packs.clear();
        try (Stream<Path> paths = Files.walk(packPath)) {
            paths
                    .filter(Files::isDirectory)
                    .filter(path -> Files.exists(Paths.get(path.toString(), "pack.json")))
                    .forEach(path -> {
                        try {
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
        if (pack == null || !pack.isEditable() || !PackObject.NAME_PATTERN.matcher(
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
        if (!PackObject.NAME_PATTERN.matcher(pack.getName()).matches()) return;
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

    public void export(String variation) throws IOException {
        if (hasNoPreset())
            return;
        if (!PackObject.NAME_PATTERN.matcher(variation).matches())
            return;
        var output = Paths.get(ItemMods.getTempPath().toString(), "output", variation);
        if (Files.exists(output))
            Files.walk(output).sorted(Comparator.reverseOrder())
                    .forEach((path) -> {
                        try {
                            Files.delete(path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        Files.createDirectories(output);
        var defaultPack = Paths.get(presetPath.toString(), "default");
        Files.walk(defaultPack).sorted(Comparator.reverseOrder()).filter(Files::isRegularFile)
                .forEach((path) -> {
                    try {
                        var current = Paths.get(output.toString(), defaultPack.relativize(path).toString());
                        Files.createDirectories(current.getParent());
                        Files.copy(path, current, StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        var variationPack = Paths.get(presetPath.toString(), "default");
        if (Files.exists(variationPack))
            Files.walk(variationPack).sorted(Comparator.reverseOrder()).filter(Files::isRegularFile)
                    .forEach((path) -> {
                        try {
                            var current = Paths.get(output.toString(), variationPack.relativize(path).toString());
                            Files.createDirectories(current.getParent());
                            Files.copy(path, current, StandardCopyOption.REPLACE_EXISTING);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        var packMeta = ItemMods.GSON.fromJson(Files.readString(Paths.get(output.toString(), "pack.mcmeta")), JsonObject.class);
        var packFormat = packMeta.getAsJsonObject("pack").get("pack_format").getAsInt();
        for (ItemModsPack pack : packs) pack.export(variation, packFormat, output);
        pack(output, Paths.get(ItemMods.getTempPath().toString(), "output.zip"));
    }

    public boolean isActivated(String name) {
        return packs.stream().anyMatch(pack -> pack.getName().equals(name));
    }

    public void zip(String name) throws IOException {
        if (getPack(name) == null && PackObject.NAME_PATTERN.matcher(name).matches())
            throw new UnsupportedOperationException();
        pack(Paths.get(getPackPath().toString(), name), Paths.get(ItemMods.getTempPath().toString(), name + ".zip"));
    }
}
