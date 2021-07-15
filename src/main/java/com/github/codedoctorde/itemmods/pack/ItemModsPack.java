package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
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
    private final List<PackTexture> textures = new ArrayList<>();
    private ItemStack icon = new ItemStack(Material.GRASS_BLOCK);
    private String description = "";

    public ItemModsPack(String name, boolean editable) {
        this.editable = editable;
        setName(name);
    }

    public ItemModsPack(String name) {
        this(name, true);
    }

    public List<String> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    public List<ItemAsset> getStaticItems() {
        return Collections.unmodifiableList(items);
    }

    public List<BlockAsset> getBlocks() {
        return blocks;
    }

    public List<CustomTemplate> getTemplates() {
        return templates;
    }

    public List<ItemAsset> getItems() {
        return items;
    }

    public List<PackTexture> getTextures() {
        return textures;
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

    public GuiItem getGuiItem(PackObject packObject) {
        return null;
    }

    public void load(Path path) throws IOException {
        JsonObject jsonObject = GSON.fromJson(Files.newBufferedReader(Paths.get(path.toString(), "config.json")), JsonObject.class);
        icon = new ItemStackBuilder(jsonObject.get("icon")).build();
    }

    public void save(Path path) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("icon", new JsonPrimitive((new ItemStackBuilder(icon)).serialize()));
        Files.writeString(Paths.get(path.toString(), "config.json"), GSON.toJson(jsonObject));
    }

    public void export(int packFormat, Path path) throws IOException {

    }

    public CustomTemplate getTemplate(String name) {
        return templates.stream().filter(packItem -> packItem.getName().equals(name)).findFirst().orElse(null);
    }
}
