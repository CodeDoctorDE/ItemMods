package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplate;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import com.github.codedoctorde.itemmods.config.CustomConfig;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public abstract class CustomTemplateData<T extends CustomTemplate<?,?>> {
    private final String name;
    private String data = "";

    public CustomTemplateData(String name) {
        this.name = name;
    }
    public CustomTemplateData(T template) {
        this(template.getClass().getName());
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public abstract T getInstance();

    public String getName() {
        return name;
    }
}
