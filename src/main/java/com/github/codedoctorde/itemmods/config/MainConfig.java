package com.github.codedoctorde.itemmods.config;

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
