package eu.vangora.itemmods.config;

import com.gitlab.codedoctorde.api.config.JsonConfigurationArray;
import com.gitlab.codedoctorde.api.config.JsonConfigurationElement;
import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class MainConfig extends JsonConfigurationElement {
    private List<ItemConfig> items = new ArrayList<>();

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
        config.setValue(new JsonConfigurationArray(items.toArray(new JsonConfigurationElement[0])), "blocks");
        return config.getElement();
    }

    @Override
    public void fromElement(JsonElement element) {
        JsonConfigurationSection config = new JsonConfigurationSection(element.getAsJsonObject());
        config.getArray("blocks").getList().forEach(block -> items.add(new ItemConfig(block.getElement())));

    }

    public List<ItemConfig> getItems() {
        return items;
    }
}
