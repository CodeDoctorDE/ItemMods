package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.CustomTemplateData;

/**
 * @author CodeDoctorDE
 */
public class CustomItemTemplateData extends CustomTemplateData<CustomItemTemplate> {
    public CustomItemTemplateData(String name) {
        super(name);
    }

    public CustomItemTemplateData(CustomItemTemplate template) {
        super(template);
    }

    @Override
    public CustomItemTemplate getInstance() {
        return ItemMods.getApi().getItemTemplate(getName());
    }
}
