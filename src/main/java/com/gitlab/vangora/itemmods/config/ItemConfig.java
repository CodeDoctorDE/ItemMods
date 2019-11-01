package com.gitlab.vangora.itemmods.config;

import com.gitlab.codedoctorde.api.config.JsonConfigurationElement;
import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonElement;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemConfig extends JsonConfigurationElement {
    private String  name;
    private ItemStack itemStack = new ItemStack(Material.GRASS_BLOCK);


    public ItemConfig(JsonElement element) {
        fromElement(element);
    }

    public ItemConfig(String name) {
        this.name = name;
    }


    @Override
    public JsonElement getElement() {
        JsonConfigurationSection config = new JsonConfigurationSection();
        config.setValue(name,"name");
        config.setValue(new ItemStackBuilder(itemStack).serialize(), "itemStack");
        return config.getElement();
    }

    @Override
    public void fromElement(JsonElement element) {
        JsonConfigurationSection config = new JsonConfigurationSection(element.getAsJsonObject());
        itemStack =new ItemStackBuilder(config.getValue("itemStack")).build();
        name = config.getString("name");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
