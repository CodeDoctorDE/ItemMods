package eu.vangora.itemmods.config;

import com.gitlab.codedoctorde.api.config.JsonConfigurationArray;
import com.gitlab.codedoctorde.api.config.JsonConfigurationElement;
import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class MainConfig extends JsonConfigurationElement {
    private List<ItemConfig> items = new ArrayList<>();
    private List<BlockConfig> blocks = new ArrayList<>();

    public MainConfig(JsonConfigurationSection baseConfig) {
        try {
            fromElement(baseConfig.getElement());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public MainConfig() {

    }

    @Override
    public JsonElement getElement() {
        JsonConfigurationSection config = new JsonConfigurationSection();
        config.setValue(new JsonConfigurationArray(items.toArray(new JsonConfigurationElement[0])), "items");
        config.setValue(new JsonConfigurationArray(blocks.toArray(new JsonConfigurationElement[0])), "blocks");
        return config.getElement();
    }

    @Override
    public void fromElement(JsonElement element) {
        JsonConfigurationSection config = new JsonConfigurationSection(element.getAsJsonObject());
        config.getArray("blocks").getList().forEach(block -> blocks.add(new BlockConfig(block.getElement())));
        config.getArray("items").getList().forEach(item -> items.add(new ItemConfig(item.getElement())));

    }

    public List<ItemConfig> getItems() {
        return items;
    }

    public List<BlockConfig> getBlocks() {
        return blocks;
    }
}
