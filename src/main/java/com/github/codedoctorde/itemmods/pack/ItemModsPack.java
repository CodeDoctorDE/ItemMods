package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.api.serializer.ItemStackTypeAdapter;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ItemModsPack extends NamedPackObject {
    private final boolean temporary;
    private final List<ItemAsset> items = new ArrayList<>();
    private final List<BlockAsset> blocks = new ArrayList<>();
    private final List<String> dependencies = new ArrayList<>();
    private final List<PackTexture> textures = new ArrayList<>();
    private ItemStack icon;
    private String description;

    public ItemModsPack(boolean temporary) {
        this.temporary = temporary;
    }

    public ItemModsPack() {
        temporary = false;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public List<ItemAsset> getStaticItems() {
        return items;
    }

    public boolean isTemporary() {
        return temporary;
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
}
