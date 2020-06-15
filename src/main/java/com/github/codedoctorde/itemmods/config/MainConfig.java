package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.Main;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainConfig {
    private final List<ItemConfig> items = new ArrayList<>();
    private final List<BlockConfig> blocks = new ArrayList<>();
    private final DatabaseConfig databaseConfig = new DatabaseConfig();

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

    public List<String> getBlockTags() {
        return blocks.stream().map(BlockConfig::getTag).collect(Collectors.toList());
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    @Nullable
    public BlockConfig getBlock(String tag) {
        return blocks.stream().filter(blockConfig -> blockConfig.getTag().equals(tag)).findFirst().orElse(null);
    }

    public boolean newBlock(String name) {
        BlockConfig blockConfig = new BlockConfig(name);
        if (getBlock(blockConfig.getTag()) != null)
            return false;
        getBlocks().add(blockConfig);
        Main.getPlugin().saveBaseConfig();
        return true;
    }

    @Nullable
    public ItemConfig getItem(String tag) {
        return items.stream().filter(itemConfig -> itemConfig.getTag().equals(tag)).findFirst().orElse(null);
    }
}
