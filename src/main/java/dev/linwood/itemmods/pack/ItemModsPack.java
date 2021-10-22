package dev.linwood.itemmods.pack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.linwood.api.utils.FileUtils;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.action.pack.PackAction;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import dev.linwood.itemmods.pack.asset.DefinedNamedPackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import dev.linwood.itemmods.pack.asset.raw.TextureAsset;
import dev.linwood.itemmods.pack.custom.CustomAssetGenerator;
import dev.linwood.itemmods.pack.custom.CustomTemplate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ItemModsPack extends DefinedNamedPackObject implements DisplayedObject {
    public static final Pattern NAME_PATTERN = Pattern.compile("^[a-z_\\-]+$");
    private final boolean editable;


    private final List<PackContent<ItemAsset>> items = new ArrayList<>();
    private final List<CustomAssetGenerator<ItemAsset>> itemGenerators = new ArrayList<>();
    private final List<PackContent<BlockAsset>> blocks = new ArrayList<>();
    private final List<CustomAssetGenerator<BlockAsset>> blockGenerators = new ArrayList<>();
    private final List<PackContent<TextureAsset>> textures = new ArrayList<>();
    private final List<String> dependencies = new ArrayList<>();
    private final List<CustomTemplate> templates = new ArrayList<>();
    private final List<CustomAssetGenerator<TextureAsset>> textureGenerators = new ArrayList<>();
    private final List<PackContent<ModelAsset>> models = new ArrayList<>();
    private final List<CustomAssetGenerator<ModelAsset>> modelGenerators = new ArrayList<>();
    private @NotNull Material icon = Material.GRASS_BLOCK;
    private String description = "";

    public ItemModsPack(@NotNull Path path) throws IOException {
        super(path.getFileName().toString());
        var br = Files.newBufferedReader(Paths.get(path.toString(), "pack.json"));
        JsonObject jsonObject = GSON.fromJson(br, JsonObject.class);
        br.close();
        if (jsonObject.has("icon") && jsonObject.get("icon").isJsonPrimitive())
            icon = Objects.requireNonNull(Material.getMaterial(jsonObject.get("icon").getAsString()));
        jsonObject.getAsJsonArray("dependencies").forEach(jsonElement -> dependencies.add(jsonElement.getAsString()));

        var itemsPath = Paths.get(path.toString(), "items");
        items.clear();
        items.addAll(loadContents(itemsPath, ItemAsset.class));

        var blocksPath = Paths.get(path.toString(), "blocks");
        blocks.clear();
        blocks.addAll(loadContents(blocksPath, BlockAsset.class));

        var modelsPath = Paths.get(path.toString(), "models");
        models.clear();
        models.addAll(loadContents(modelsPath, ModelAsset.class));

        var texturesPath = Paths.get(path.toString(), "textures");
        textures.clear();
        textures.addAll(loadContents(texturesPath, TextureAsset.class));
        editable = true;
    }

    public ItemModsPack(@NotNull String name, boolean editable) throws UnsupportedOperationException {
        super(name);
        this.editable = editable;
    }

    public ItemModsPack(@NotNull String name) {
        this(name, true);
    }

    private <T extends PackAsset> PackContent<T> loadContent(Path path, Class<T> assetClass) {
        try {
            var br = Files.newBufferedReader(path);
            var jsonObject = GSON.fromJson(br, JsonObject.class);
            var assetName = FileUtils.getFileName(path);
            return new PackContent<>(new PackObject(name, assetName), jsonObject, assetClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T extends PackAsset> List<PackContent<T>> loadContents(Path path, Class<T> assetClass) {
        try {
            return Files.walk(path).filter(Files::isRegularFile).map(current -> loadContent(current, assetClass)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public @NotNull List<ItemAsset> getItems() {
        return items.stream().map(PackContent::getAsset).collect(Collectors.toList());
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

    public void registerItem(@NotNull PackObject generator, @NotNull ItemAsset itemAsset) {
        if (PackObject.NAME_PATTERN.matcher(itemAsset.getName()).matches())
            items.add(new PackContent<>(itemAsset, this, generator, ItemAsset.class));
    }

    public @NotNull List<BlockAsset> getBlocks() {
        return blocks.stream().map(PackContent::getAsset).collect(Collectors.toList());
    }

    public void unregisterItem(String name) {
        items.removeIf(itemAsset -> itemAsset.getName().equals(name));
    }

    public void registerBlock(@NotNull PackObject generator, @NotNull BlockAsset blockAsset) {
        if (PackObject.NAME_PATTERN.matcher(blockAsset.getName()).matches())
            blocks.add(new PackContent<>(blockAsset, this, generator, BlockAsset.class));
    }

    public @NotNull List<TextureAsset> getTextures() {
        return textures.stream().map(PackContent::getAsset).collect(Collectors.toList());
    }

    public void unregisterBlock(String name) {
        blocks.removeIf(blockAsset -> blockAsset.getName().equals(name));
    }

    public void registerTexture(@NotNull PackObject generator, @NotNull TextureAsset textureAsset) {
        if (PackObject.NAME_PATTERN.matcher(textureAsset.getName()).matches())
            textures.add(new PackContent<>(textureAsset, this, generator, TextureAsset.class));
    }

    public @NotNull List<ModelAsset> getModels() {
        return models.stream().map(PackContent::getAsset).collect(Collectors.toList());
    }

    public void unregisterTexture(String name) {
        textures.removeIf(textureAsset -> textureAsset.getName().equals(name));
    }

    public void registerModel(@NotNull PackObject generator, @NotNull ModelAsset modelAsset) {
        if (PackObject.NAME_PATTERN.matcher(modelAsset.getName()).matches())
            models.add(new PackContent<>(modelAsset, this, generator, ModelAsset.class));
    }

    @Nullable
    public BlockAsset getBlock(String name) {
        return blocks.stream().filter(blockAsset -> blockAsset.getName().equals(name)).map(PackContent::getAsset).findFirst().orElse(null);
    }

    public void unregisterModel(String name) {
        models.removeIf(modelAsset -> modelAsset.getName().equals(name));
    }

    public @NotNull List<CustomTemplate> getTemplates() {
        return Collections.unmodifiableList(templates);
    }

    public void registerTemplate(@NotNull CustomTemplate customTemplate) {
        if (PackObject.NAME_PATTERN.matcher(customTemplate.getName()).matches())
            templates.add(customTemplate);
    }

    public void unregisterTemplate(String name) {
        templates.removeIf(templateAsset -> templateAsset.getName().equals(name));
    }

    public @NotNull List<CustomAssetGenerator<ItemAsset>> getItemGenerators() {
        return Collections.unmodifiableList(itemGenerators);
    }

    public void registerItemGenerator(@NotNull CustomAssetGenerator<ItemAsset> generator) {
        if (PackObject.NAME_PATTERN.matcher(generator.getName()).matches())
            itemGenerators.add(generator);
    }

    public void unregisterItemGenerator(String name) {
        itemGenerators.removeIf(generator -> generator.getName().equals(name));
    }

    public @NotNull List<CustomAssetGenerator<BlockAsset>> getBlockGenerators() {
        return Collections.unmodifiableList(blockGenerators);
    }

    public void registerBlockGenerator(@NotNull CustomAssetGenerator<BlockAsset> generator) {
        if (PackObject.NAME_PATTERN.matcher(generator.getName()).matches())
            blockGenerators.add(generator);
    }

    public void unregisterBlockGenerator(String name) {
        blockGenerators.removeIf(generator -> generator.getName().equals(name));
    }

    public @NotNull List<CustomAssetGenerator<TextureAsset>> getTextureGenerators() {
        return Collections.unmodifiableList(textureGenerators);
    }

    public void registerModelGenerator(@NotNull CustomAssetGenerator<ModelAsset> generator) {
        if (PackObject.NAME_PATTERN.matcher(generator.getName()).matches())
            modelGenerators.add(generator);
    }

    public void unregisterModelGenerator(String name) {
        modelGenerators.removeIf(generator -> generator.getName().equals(name));
    }

    public void registerTextureGenerator(@NotNull CustomAssetGenerator<TextureAsset> generator) {
        if (PackObject.NAME_PATTERN.matcher(generator.getName()).matches())
            textureGenerators.add(generator);
    }

    public void unregisterTextureGenerator(String name) {
        textureGenerators.removeIf(generator -> generator.getName().equals(name));
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

    @Nullable
    public ItemAsset getItem(String name) {
        return items.stream().filter(packItem -> packItem.getName().equals(name)).map(PackContent::getAsset).findFirst().orElse(null);
    }

    @Nullable
    public ModelAsset getModel(String name) {
        return models.stream().filter(modelAsset -> modelAsset.getName().equals(name)).map(PackContent::getAsset).findFirst().orElse(null);
    }

    @Nullable
    public TextureAsset getTexture(String name) {
        return textures.stream().filter(textureAsset -> textureAsset.getName().equals(name)).map(PackContent::getAsset).findFirst().orElse(null);
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
        items.forEach(itemAsset -> {
            var current = itemAsset.save();
            if (current != null) try {
                var currentPath = Paths.get(itemsDir.toString(), itemAsset.getName() + ".json");
                Files.createDirectories(currentPath.getParent());
                Files.writeString(currentPath, GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var blocksDir = Paths.get(path.toString(), "blocks");
        if (!Files.exists(blocksDir))
            Files.createDirectories(blocksDir);
        blocks.forEach(blockAsset -> {
            var current = blockAsset.save();
            if (current != null) try {
                var currentPath = Paths.get(blocksDir.toString(), blockAsset.getName() + ".json");
                Files.createDirectories(currentPath.getParent());
                Files.writeString(currentPath, GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var texturesDir = Paths.get(path.toString(), "textures");
        if (!Files.exists(texturesDir))
            Files.createDirectories(texturesDir);
        textures.forEach(textureAsset -> {
            var current = textureAsset.save();
            if (current != null) try {
                var currentPath = Paths.get(texturesDir.toString(), textureAsset.getName() + ".json");
                Files.createDirectories(currentPath.getParent());
                Files.writeString(Paths.get(texturesDir.toString(), textureAsset.getName() + ".json"), GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var modelsDir = Paths.get(path.toString(), "models");
        if (!Files.exists(modelsDir))
            Files.createDirectories(modelsDir);
        models.forEach(modelAsset -> {
            var current = modelAsset.save();
            if (current != null) try {
                var currentPath = Paths.get(modelsDir.toString(), modelAsset.getName() + ".json");
                Files.createDirectories(currentPath.getParent());
                Files.writeString(currentPath, GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Nullable
    public CustomAssetGenerator<ItemAsset> getItemGenerator(String name) {
        return itemGenerators.stream().filter(generator -> generator.getName().equals(name)).findFirst().orElse(null);
    }

    @Nullable
    public CustomAssetGenerator<BlockAsset> getBlockGenerator(String name) {
        return blockGenerators.stream().filter(generator -> generator.getName().equals(name)).findFirst().orElse(null);
    }

    @Nullable
    public CustomAssetGenerator<ModelAsset> getModelGenerator(String name) {
        return modelGenerators.stream().filter(generator -> generator.getName().equals(name)).findFirst().orElse(null);
    }

    @Nullable
    public CustomAssetGenerator<TextureAsset> getTextureGenerator(String name) {
        return textureGenerators.stream().filter(generator -> generator.getName().equals(name)).findFirst().orElse(null);
    }

    public void export(String variation, int packFormat, @NotNull Path path) throws IOException {
        for (PackContent<ModelAsset> model : models) model.getAsset().export(getName(), variation, packFormat, path);
        for (PackContent<TextureAsset> texture : textures)
            texture.getAsset().export(getName(), variation, packFormat, path);
    }

    private static class PackContent<T extends PackAsset> {
        private final T asset;
        private final PackObject generator;
        private final ItemModsPack pack;
        private final Class<T> assetClass;


        public PackContent(T asset, ItemModsPack pack, PackObject generator, Class<T> assetClass) {
            this.asset = asset;
            this.pack = pack;
            this.generator = generator;
            this.assetClass = assetClass;
        }

        public PackContent(PackObject packObject, JsonObject jsonObject, Class<T> assetClass) {
            generator = new PackObject(jsonObject.get("generator").getAsString());
            this.assetClass = assetClass;
            this.pack = packObject.getPack();
            asset = getCustomAssetGenerator().loadAsset(
                    packObject,
                    jsonObject.get("asset").getAsJsonObject());
        }

        public PackObject getPackObject() {
            return new PackObject(pack.getName(), asset.getName());
        }

        public PackObject getGenerator() {
            return generator;
        }

        public T getAsset() {
            return asset;
        }

        public CustomAssetGenerator<T> getCustomAssetGenerator() {
            return generator.getGeneratorByType(assetClass);
        }

        @Nullable
        public JsonObject save() {
            var saveData = getCustomAssetGenerator().save(pack.getName(), asset);
            if (saveData == null)
                return null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("asset", saveData);
            jsonObject.addProperty("generator", generator.toString());
            return jsonObject;
        }

        public String getName() {
            return asset.getName();
        }
    }

    public CustomTemplate getTemplate(String name) {
        return templates.stream().filter(packItem -> packItem.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public void setName(@NotNull String name) throws UnsupportedOperationException {
        if (!NAME_PATTERN.matcher(name).matches())
            throw new UnsupportedOperationException();
        this.name = name;
    }

    @Override
    public @Nullable CommandAction generateAction() {
        return new PackAction(name);
    }
}
