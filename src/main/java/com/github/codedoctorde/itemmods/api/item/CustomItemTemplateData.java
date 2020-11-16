package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplate;
import org.jetbrains.annotations.NotNull;
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

    @NotNull
    public CustomItemTemplate getInstance() throws ClassNotFoundException {
        return ItemMods.getPlugin().getApi().getItemTemplate(name);
    }

    public String getName() {
        return name;
    }
}
