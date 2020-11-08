package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.ItemMods;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainConfig {
    private final List<ItemConfig> items = new ArrayList<>();
    private final List<BlockConfig> blocks = new ArrayList<>();
    private final DatabaseConfig databaseConfig = new DatabaseConfig();
    private final ResourcePackConfig resourcePackConfig = new ResourcePackConfig();

    public MainConfig() {

    }

    public List<ItemConfig> getItems() {
        return items;
    }

    public List<String> getItemTags() {
        return items.stream().map(ItemConfig::getTag).collect(Collectors.toList());
    }

    public List<BlockConfig> getBlocks() {
        return blocks;
    }

    public List<String> getBlockNames(String namespace) {
        return blocks.stream().filter(blockConfig -> blockConfig.getNamespace().equals(namespace)).map(BlockConfig::getName).collect(Collectors.toList());
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    @Nullable
    public BlockConfig getBlock(String namespace, String name) {
        return blocks.stream().filter(blockConfig -> blockConfig.getNamespace().equals(namespace) && blockConfig.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean createBlock(String namespace, String name) {
        BlockConfig blockConfig = new BlockConfig(namespace, name);
        if (getBlock(namespace, name) != null)
            return false;
        getBlocks().add(blockConfig);
        ItemMods.getPlugin().saveBaseConfig();
        return true;
    }

    @Nullable
    public ItemConfig getItem(String tag) {
        return items.stream().filter(itemConfig -> itemConfig.getTag().equals(tag)).findFirst().orElse(null);
    }

    public ResourcePackConfig getResourcePackConfig() {
        return resourcePackConfig;
    }
}
