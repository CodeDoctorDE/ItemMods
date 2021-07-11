package com.github.codedoctorde.itemmods.pack;

import org.bukkit.inventory.ItemStack;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ItemModsPack extends NamedPackObject {
    private final boolean temporary;
    private final List<CustomItem> customItems = new ArrayList<>();
    private final List<StaticBlock> staticBlocks = new ArrayList<>();
    private final List<String> dependencies = new ArrayList<>();
    private final List<PackTexture> textures = new ArrayList<>();
    private ItemStack icon;

    public ItemModsPack(boolean temporary) {
        this.temporary = temporary;
    }

    public ItemModsPack() {
        temporary = false;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public List<CustomItem> getStaticItems() {
        return customItems;
    }

    public boolean isTemporary() {
        return temporary;
    }

    void save(ItemModsPack pack, Path path) {

    }

    @Override
    void load(ItemModsPack pack, Path path) {

    }

    @Override
    void export(ItemModsPack pack, Path path) {

    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }
}
