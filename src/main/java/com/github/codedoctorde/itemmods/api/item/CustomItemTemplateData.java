package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplate;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public class CustomItemTemplateData {
    private String name;
    private String data = "";

    public CustomItemTemplateData(){

    }
    public CustomItemTemplateData(String name) {
        this.name = name;
    }
    public CustomItemTemplateData(CustomItemTemplate template){
        this(template.getClass().getName());
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public CustomItemTemplate getInstance() {
        if (name == null)
            return null;
        try {
            return ItemMods.getPlugin().getApi().getItemTemplate(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        name = null;
        return null;
    }

    public void setInstance(@Nullable CustomItemTemplate blockTemplate) {
        name = blockTemplate == null ? null : blockTemplate.getClass().getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
