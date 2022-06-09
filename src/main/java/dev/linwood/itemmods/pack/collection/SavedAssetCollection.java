package dev.linwood.itemmods.pack.collection;

import com.google.gson.JsonElement;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.ItemModsPack;
import dev.linwood.itemmods.pack.asset.PackAsset;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class SavedAssetCollection<T extends PackAsset> extends AssetCollection<T> {
    private final BiFunction<String, JsonElement, T> assetFactory;
    private final @Nullable String directoryName;

    public SavedAssetCollection(ItemModsPack parent, @Nullable String directoryName, BiFunction<String, JsonElement, T> assetFactory) throws IOException {
        this(parent, new HashSet<>(), directoryName, assetFactory);
    }

    public SavedAssetCollection(ItemModsPack parent, Set<T> assets, @Nullable String directoryName, BiFunction<String, JsonElement, T> assetFactory) throws IOException {
        super(parent, assets);
        this.directoryName = directoryName;
        this.assetFactory = assetFactory;
    }

    public Path getDirectoryPath() {
        if (directoryName == null) {
            return null;
        }
        return Paths.get(ItemMods.getPackManager().getPackPath().toString(), parent.getName(), directoryName);
    }

    public void reload() throws IOException {
        super.reload();
        if (directoryName == null) {
            return;
        }
        createDirectory();
        Files.list(getDirectoryPath()).filter(Files::isRegularFile).forEach(path -> {
            T asset;
            String fileName = path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf('.'));
            try {
                asset = assetFactory.apply(fileName, ItemMods.GSON.fromJson(Files.newBufferedReader(path), JsonElement.class));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (asset != null) {
                assets.add(asset);
            }
        });
    }

    public void createDirectory() throws IOException {
        if (directoryName == null) {
            return;
        }
        Files.createDirectories(getDirectoryPath());
    }

    public void save(T asset) throws IOException {
        if (directoryName == null) {
            return;
        }
        createDirectory();
        Files.write(getDirectoryPath().resolve(asset.getName() + ".json"), asset.save(parent.getName()).toString().getBytes());
    }

    public void save() {
        if (directoryName == null) {
            return;
        }
        assets.forEach(asset -> {
            try {
                save(asset);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
