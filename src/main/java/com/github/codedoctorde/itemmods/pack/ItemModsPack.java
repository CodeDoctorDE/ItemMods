package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.pack.asset.BlockAsset;
import com.github.codedoctorde.itemmods.pack.asset.ItemAsset;
import com.github.codedoctorde.itemmods.pack.asset.raw.ModelAsset;
import com.github.codedoctorde.itemmods.pack.asset.raw.TextureAsset;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemModsPack extends NamedPackObject {
    private final boolean editable;
    private final List<ItemAsset> items = new ArrayList<>();
    private final List<BlockAsset> blocks = new ArrayList<>();
    private final List<String> dependencies = new ArrayList<>();
    private final List<CustomTemplate> templates = new ArrayList<>();
    private final List<TextureAsset> textures = new ArrayList<>();
    private final List<ModelAsset> models = new ArrayList<>();
    private final List<ModelAsset> modelAssets = new ArrayList<>();
    private ItemStack icon = new ItemStack(Material.GRASS_BLOCK);
    private String description = "";

    public ItemModsPack(String name, boolean editable) throws UnsupportedOperationException {
        super(name);
        this.editable = editable;
    }

    public ItemModsPack(String name) {
        this(name, true);
    }


    public ItemModsPack(Path path) throws IOException {
        super(path.getFileName().toString());
        editable = true;
        JsonObject jsonObject = GSON.fromJson(Files.newBufferedReader(Paths.get(path.toString(), "pack.json")), JsonObject.class);
        icon = new ItemStackBuilder(jsonObject.get("icon")).build();
        jsonObject.getAsJsonArray("dependencies").forEach(jsonElement -> dependencies.add(jsonElement.getAsString()));

        Files.walk(Paths.get(path.toString(), "items")).filter(Files::isRegularFile).forEach(current -> {
            try {
                items.add(new ItemAsset(new PackObject(getName(), getFileName(path)), GSON.fromJson(Files.readString(current), JsonObject.class)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<String> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    public void registerDependency(String name) {
        if (NamedPackObject.NAME_PATTERN.matcher(name).matches())
            dependencies.add(name);
    }

    public void unregisterDependency(String name) {
        dependencies.removeIf(dependency -> dependency.equals(name));
    }

    public List<ItemAsset> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void registerItem(ItemAsset itemAsset) {
        if (NamedPackObject.NAME_PATTERN.matcher(itemAsset.getName()).matches())
            items.add(itemAsset);
    }

    public void unregisterItem(String name) {
        items.removeIf(itemAsset -> itemAsset.getName().equals(name));
    }

    public List<BlockAsset> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    public void registerBlock(BlockAsset blockAsset) {
        if (NamedPackObject.NAME_PATTERN.matcher(blockAsset.getName()).matches())
            blocks.add(blockAsset);
    }

    public void unregisterBlock(String name) {
        blocks.removeIf(blockAsset -> blockAsset.getName().equals(name));
    }

    public List<TextureAsset> getTextures() {
        return Collections.unmodifiableList(textures);
    }

    public void registerTexture(TextureAsset textureAsset) {
        if (NamedPackObject.NAME_PATTERN.matcher(textureAsset.getName()).matches())
            textures.add(textureAsset);
    }

    public void unregisterTexture(String name) {
        textures.removeIf(textureAsset -> textureAsset.getName().equals(name));
    }

    public List<ModelAsset> getModels() {
        return Collections.unmodifiableList(models);
    }

    public void registerModel(ModelAsset modelAsset) {
        if (NamedPackObject.NAME_PATTERN.matcher(modelAsset.getName()).matches())
            models.add(modelAsset);
    }

    public void unregisterModel(String name) {
        models.removeIf(modelAsset -> modelAsset.getName().equals(name));
    }

    public List<CustomTemplate> getTemplates() {
        return Collections.unmodifiableList(templates);
    }

    public void registerTemplate(CustomTemplate customTemplate) {
        if (NamedPackObject.NAME_PATTERN.matcher(customTemplate.getName()).matches())
            templates.add(customTemplate);
    }

    public void unregisterTemplate(String name) {
        templates.removeIf(templateAsset -> templateAsset.getName().equals(name));
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

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    @Nullable
    public BlockAsset getBlock(String name) {
        return blocks.stream().filter(blockAsset -> blockAsset.getName().equals(name)).findFirst().orElse(null);
    }

    @Nullable
    public ItemAsset getItem(String name) {
        return items.stream().filter(packItem -> packItem.getName().equals(name)).findFirst().orElse(null);
    }

    public ModelAsset getModel(String name) {
        return models.stream().filter(modelAsset -> modelAsset.getName().equals(name)).findFirst().orElse(null);
    }

    public GuiItem getGuiItem(PackObject packObject) {
        return null;
    }

    private String getFileName(Path path) {
        var pathName = path.getFileName().toString();
        var pos = pathName.lastIndexOf('.');
        if (pos > 0) return pathName.substring(0, pos);
        return null;
    }

    void save(Path path) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("icon", new JsonPrimitive((new ItemStackBuilder(icon)).serialize()));
        var dependenciesArray = new JsonArray();
        dependencies.forEach(dependenciesArray::add);
        jsonObject.add("dependencies", dependenciesArray);
        Files.writeString(Paths.get(path.toString(), "pack.json"), GSON.toJson(jsonObject));

        var itemsDir = Paths.get(path.toString(), "items");
        if (!Files.exists(itemsDir))
            Files.createDirectories(itemsDir);
        items.forEach(itemAsset -> {
            var current = itemAsset.save(getName());
            try {
                Files.writeString(Paths.get(itemsDir.toString(), itemAsset.getName() + ".json"), GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var blocksDir = Paths.get(path.toString(), "blocks");
        if (!Files.exists(blocksDir))
            Files.createDirectories(blocksDir);
        blocks.forEach(blockAsset -> {
            var current = blockAsset.save(getName());
            try {
                Files.writeString(Paths.get(blocksDir.toString(), blockAsset.getName() + ".json"), GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var texturesDir = Paths.get(path.toString(), "textures");
        if (!Files.exists(texturesDir))
            Files.createDirectories(texturesDir);
        textures.forEach(textureAsset -> {
            var current = textureAsset.save(getName());
            try {
                Files.writeString(Paths.get(texturesDir.toString(), textureAsset.getName() + ".json"), GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var modelsDir = Paths.get(path.toString(), "models");
        if (!Files.exists(modelsDir))
            Files.createDirectories(modelsDir);
        models.forEach(modelAsset -> {
            var current = modelAsset.save(getName());
            try {
                Files.writeString(Paths.get(modelsDir.toString(), modelAsset.getName() + ".json"), GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void export(int packFormat, Path path) throws IOException {

    }

    public CustomTemplate getTemplate(String name) {
        return templates.stream().filter(packItem -> packItem.getName().equals(name)).findFirst().orElse(null);
    }
}
