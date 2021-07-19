package com.github.codedoctorde.itemmods.config;

import org.jetbrains.annotations.NotNull;

public class MainConfig {
    private final DatabaseConfig databaseConfig = new DatabaseConfig();
    private final ResourcePackConfig resourcePackConfig = new ResourcePackConfig();

    public MainConfig() {

    }

    public @NotNull DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public @NotNull ResourcePackConfig getResourcePackConfig() {
        return resourcePackConfig;
    }
}
