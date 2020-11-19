package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplate;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public abstract class CustomTemplateData<T extends CustomTemplate> {
    private String name;
    private String data = "";

    public CustomTemplateData(String name) {
        this.name = name;
    }
    public CustomTemplateData(CustomBlockTemplate template) {
        this(template.getClass().getName());
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public CustomBlockTemplate getInstance() {
        if (name == null)
            return null;
        try {
            return ItemMods.getPlugin().getApi().getBlockTemplate(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        name = null;
        return null;
    }

    public void setInstance(@Nullable CustomBlockTemplate blockTemplate) {
        if (blockTemplate == null)
            name = null;
        else
            name = blockTemplate.getClass().getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
