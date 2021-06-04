package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.ItemMods;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    public Set<String> getItemNames(String namespace) {
        return items.stream().filter(itemConfig -> itemConfig.getNamespace().equals(namespace)).map(ItemConfig::getName).collect(Collectors.toSet());
    }

    public List<BlockConfig> getBlocks() {
        return blocks;
    }

    public Set<String> getBlockNames(String namespace) {
        return blocks.stream().filter(blockConfig -> blockConfig.getNamespace().equals(namespace)).map(BlockConfig::getName).collect(Collectors.toSet());
    }

    public Set<String> getNamespaces() {
        Set<String> namespaces = blocks.stream().map(BlockConfig::getNamespace).collect(Collectors.toSet());
        items.stream().map(ItemConfig::getNamespace).forEach(namespaces::add);
        return namespaces;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    @Nullable
    public BlockConfig getBlock(String identifier) {
        return blocks.stream().filter(blockConfig -> blockConfig.getIdentifier().equals(identifier)).findFirst().orElse(null);
    }

    public boolean createBlock(String namespace, String name) {
        BlockConfig blockConfig = new BlockConfig(namespace, name);
        if (getBlock(blockConfig.getIdentifier()) != null)
            return false;
        getBlocks().add(blockConfig);
        ItemMods.saveBaseConfig();
        return true;
    }

    @Nullable
    public ItemConfig getItem(String identifier) {
        return items.stream().filter(itemConfig -> itemConfig.getIdentifier().equals(identifier)).findFirst().orElse(null);
    }

    public ResourcePackConfig getResourcePackConfig() {
        return resourcePackConfig;
    }
}
