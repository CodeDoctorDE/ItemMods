package com.github.codedoctorde.itemmods.config;

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
}
