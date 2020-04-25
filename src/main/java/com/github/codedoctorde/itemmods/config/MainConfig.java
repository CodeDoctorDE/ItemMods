package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.Main;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainConfig {
    private List<ItemConfig> items = new ArrayList<>();
    private List<BlockConfig> blocks = new ArrayList<>();
    private DatabaseConfig databaseConfig = new DatabaseConfig();

    public MainConfig() {

    }

    public List<ItemConfig> getItems() {
        return items;
    }

    public List<BlockConfig> getBlocks() {
        return blocks;
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
}
