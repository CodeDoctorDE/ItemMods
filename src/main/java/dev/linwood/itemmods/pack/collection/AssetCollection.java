package dev.linwood.itemmods.pack.collection;

import dev.linwood.itemmods.pack.ItemModsPack;
import dev.linwood.itemmods.pack.asset.PackAsset;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class AssetCollection<T extends PackAsset> {
    protected final ItemModsPack parent;
    protected final Set<T> assets;

    public AssetCollection(ItemModsPack parent) throws IOException {
        this(parent, new HashSet<>());
    }

    public AssetCollection(ItemModsPack parent, Set<T> assets) throws IOException {
        this.parent = parent;
        this.assets = assets;
        reload();
    }

    public Set<T> getAssets() {
        return Collections.unmodifiableSet(assets);
    }

    public void registerAsset(T asset) {
        assets.add(asset);
    }

    public T getAsset(String name) {
        return assets.stream().filter(asset -> asset.getName().equals(name)).findFirst().orElse(null);
    }

    public void unregisterAsset(String assetName) {
        assets.removeIf(asset -> asset.getName().equals(assetName));
    }

    public Stream<T> stream() {
        return assets.stream();
    }

    public void reload() throws IOException {
        assets.clear();
    }

    public void clear() {
        assets.clear();
    }
}
