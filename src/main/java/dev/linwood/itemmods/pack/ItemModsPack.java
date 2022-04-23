package dev.linwood.itemmods.pack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import dev.linwood.itemmods.pack.asset.raw.SoundAsset;
import dev.linwood.itemmods.pack.asset.raw.TextureAsset;
import dev.linwood.itemmods.pack.collection.AssetCollection;
import dev.linwood.itemmods.pack.collection.ExportedAssetCollection;
import dev.linwood.itemmods.pack.collection.SavedAssetCollection;
import dev.linwood.itemmods.pack.custom.CustomTemplate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static dev.linwood.itemmods.ItemMods.GSON;

public class ItemModsPack implements NamedPackObject {
    public static final Pattern NAME_PATTERN = Pattern.compile("^[a-z_\\-]+$");
    private final boolean editable;
    private final List<String> dependencies = new ArrayList<>();
    private final SavedAssetCollection<ItemAsset> itemAssets;
    private final SavedAssetCollection<BlockAsset> blockAssets;
    private final ExportedAssetCollection<ModelAsset> modelAssets;
    private final ExportedAssetCollection<TextureAsset> textureAssets;
    private final ExportedAssetCollection<SoundAsset> soundAssets;
    private final AssetCollection<CustomTemplate> templateAssets;
    private String name;
    private @NotNull Material icon = Material.GRASS_BLOCK;
    private String description = "";

    public ItemModsPack(@NotNull String name, boolean editable) throws UnsupportedOperationException, IOException {
        this.name = name;
        this.editable = editable;
        itemAssets = new SavedAssetCollection<>(this, "items", (asset, element) -> new ItemAsset(asset, element.getAsJsonObject()));
        blockAssets = new SavedAssetCollection<>(this, "blocks", (asset, element) -> new BlockAsset(asset, element.getAsJsonObject()));
        modelAssets = new ExportedAssetCollection<>(this, "models", (asset, element) -> new ModelAsset(asset, element.getAsJsonObject()));
        textureAssets = new ExportedAssetCollection<>(this, "textures", (asset, element) -> new TextureAsset(asset, element.getAsJsonObject()));
        soundAssets = new ExportedAssetCollection<>(this, "sounds", (asset, element) -> new SoundAsset(asset, element.getAsJsonObject()));
        templateAssets = new AssetCollection<>(this);
    }

    public ItemModsPack(@NotNull String name) throws IOException {
        this(name, true);
    }

    public ItemModsPack(@NotNull Path path) throws IOException {
        this(path.getFileName().toString(), true);
        var br = Files.newBufferedReader(Paths.get(path.toString(), "pack.json"));
        JsonObject jsonObject = GSON.fromJson(br, JsonObject.class);
        br.close();
        if (jsonObject.has("icon") && jsonObject.get("icon").isJsonPrimitive())
            icon = Objects.requireNonNull(Material.getMaterial(jsonObject.get("icon").getAsString()));
        jsonObject.getAsJsonArray("dependencies").forEach(jsonElement -> dependencies.add(jsonElement.getAsString()));
    }

    @SuppressWarnings("unchecked")
    public <T extends PackAsset> AssetCollection<T> getCollection(@NotNull Class<T> type) {
        if (type == ItemAsset.class) {
            return (AssetCollection<T>) itemAssets;
        } else if (type == BlockAsset.class) {
            return (AssetCollection<T>) blockAssets;
        } else if (type == ModelAsset.class) {
            return (AssetCollection<T>) modelAssets;
        } else if (type == TextureAsset.class) {
            return (AssetCollection<T>) textureAssets;
        } else if (type == SoundAsset.class) {
            return (AssetCollection<T>) soundAssets;
        } else if (type == CustomTemplate.class) {
            return (AssetCollection<T>) templateAssets;
        } else {
            throw new IllegalArgumentException("Invalid asset type");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) throws UnsupportedOperationException {
        if (!NAME_PATTERN.matcher(name).matches())
            throw new UnsupportedOperationException();
        this.name = name;
    }

    public @NotNull List<String> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    public void registerDependency(@NotNull String name) {
        if (PackObject.NAME_PATTERN.matcher(name).matches())
            dependencies.add(name);
    }

    public void unregisterDependency(String name) {
        dependencies.removeIf(dependency -> dependency.equals(name));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEditable() {
        return editable;
    }

    public @NotNull ItemStack getIcon() {
        return new ItemStack(icon);
    }

    public void setIcon(@NotNull Material icon) {
        this.icon = icon;
    }

    void save(@NotNull Path path) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("icon", new JsonPrimitive(icon.name()));
        var dependenciesArray = new JsonArray();
        dependencies.forEach(dependenciesArray::add);
        jsonObject.add("dependencies", dependenciesArray);
        jsonObject.addProperty("version", ItemMods.FILE_VERSION);
        Files.writeString(Paths.get(path.toString(), "pack.json"), GSON.toJson(jsonObject));

        var itemsDir = Paths.get(path.toString(), "items");
        if (!Files.exists(itemsDir))
            Files.createDirectories(itemsDir);
        itemAssets.save();
        blockAssets.save();
        modelAssets.save();
        textureAssets.save();
        soundAssets.save();
    }

    public void export(String variation, @NotNull Path path) throws IOException {
        for (ModelAsset model : modelAssets.getAssets()) model.export(getName(), variation, path);
        for (TextureAsset texture : textureAssets.getAssets())
            texture.export(getName(), variation, path);
    }

    public <T extends PackAsset> T getAsset(Class<T> type, String name) {
        return getCollection(type).getAsset(name);
    }

    public <T extends PackAsset> void register(T asset) {
        @SuppressWarnings("unchecked")
        AssetCollection<T> collection = (AssetCollection<T>) getCollection(asset.getClass());
        collection.registerAsset(asset);
    }

    public <T extends PackAsset> void unregister(T asset) {
        @SuppressWarnings("unchecked")
        AssetCollection<T> collection = (AssetCollection<T>) getCollection(asset.getClass());
        collection.unregisterAsset(asset.getName());
    }
}
