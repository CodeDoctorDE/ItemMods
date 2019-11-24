package eu.vangora.itemmods.config;

import java.util.ArrayList;
import java.util.List;

public class MainConfig {
    private transient List<ItemConfig> items = new ArrayList<>();
    private List<BlockConfig> blocks = new ArrayList<>();

    public MainConfig() {

    }

    public List<ItemConfig> getItems() {
        return items;
    }

    public List<BlockConfig> getBlocks() {
        return blocks;
    }
}
