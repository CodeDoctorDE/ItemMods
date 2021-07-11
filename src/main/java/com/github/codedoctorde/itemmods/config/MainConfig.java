package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.ItemMods;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainConfig {
    private final DatabaseConfig databaseConfig = new DatabaseConfig();
    private final ResourcePackConfig resourcePackConfig = new ResourcePackConfig();

    public MainConfig() {

    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public ResourcePackConfig getResourcePackConfig() {
        return resourcePackConfig;
    }
}
