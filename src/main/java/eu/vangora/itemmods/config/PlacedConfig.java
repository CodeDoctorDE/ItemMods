package eu.vangora.itemmods.config;

import com.gitlab.codedoctorde.api.config.JsonConfiguration;
import com.gitlab.codedoctorde.api.config.JsonConfigurationElement;
import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.utils.ConfigUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class PlacedConfig extends JsonConfigurationElement {
    private static Gson gson = new Gson();
    private HashMap<Location, ItemConfig> itemConfigHashMap = new HashMap<>();

    public PlacedConfig(JsonConfiguration placedJsonConfig) {
        fromElement(placedJsonConfig.getElement());
    }

    public PlacedConfig() {

    }


    @Override
    public JsonElement getElement() {
        JsonConfigurationSection config = new JsonConfigurationSection();
        itemConfigHashMap.forEach((key, value) -> config.setValue(value.getElement(), ConfigUtils.locationToSection(key).getElement().toString()));
        return config.getElement();
    }

    @Override
    public void fromElement(JsonElement element) {
        JsonConfigurationSection config = new JsonConfigurationSection(element.getAsJsonObject());
        for (Map.Entry<String, JsonConfigurationElement> entry:
             config.getValues().entrySet()) {
            Location location = ConfigUtils.sectionToLocation(new JsonConfigurationSection(gson.fromJson(entry.getKey(), JsonObject.class)));
            if(!location.getBlock().getType().isEmpty())
                itemConfigHashMap.put(location, new ItemConfig(entry.getValue().getElement()));
        }
    }

    public HashMap<Location, ItemConfig> getItemConfigHashMap() {
        return itemConfigHashMap;
    }
}
