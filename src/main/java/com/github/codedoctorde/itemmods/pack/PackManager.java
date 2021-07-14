package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.itemmods.ItemMods;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author CodeDoctorDE
 */
public class PackManager {
    private static final Pattern NAME_PATTERN = Pattern.compile("/.*/");
    private final Path packPath;
    private List<ItemModsPack> packs;

    public PackManager() throws IOException {
        packPath = Paths.get(ItemMods.getPlugin().getDataFolder().getPath(), "packs");
        try {
            Files.createDirectory(packPath);
        } catch (FileAlreadyExistsException ignored) {

        }
    }

    public void reload() {
        packs.clear();
        try (Stream<Path> paths = Files.walk(packPath)) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        var pack = new ItemModsPack();
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
        if (pack == null || pack.isTemporary() || !NamedPackObject.NAME_PATTERN.matcher(
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

    public Set<String> getPackNames() {
        return packs.stream().map(NamedPackObject::getName).collect(Collectors.toSet());
    }

    public void registerPack(ItemModsPack pack) {
        if (!NamedPackObject.NAME_PATTERN.matcher(pack.getName()).matches())
            return;
        if (packs.stream().anyMatch(current -> current.getName().equals(pack.getName())))
            return;
        packs.add(pack);
    }

    public void unregisterPack(String name) {
        packs.removeIf(itemModsPack -> itemModsPack.getName().equals(name));
    }

    public Path getPackPath() {
        return packPath;
    }

    @Nullable
    public ItemModsPack getPack(String name) {
        return packs.stream().filter(pack -> pack.getName().equals(name)).findFirst().orElse(null);
    }
}
