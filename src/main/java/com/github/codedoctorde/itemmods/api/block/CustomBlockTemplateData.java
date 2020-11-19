package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.CustomTemplateData;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;

/**
 * @author CodeDoctorDE
 */
public class CustomBlockTemplateData extends CustomTemplateData<CustomBlockTemplate> {
    public CustomBlockTemplateData(String name) {
        super(name);
    }

    public CustomBlockTemplateData(CustomBlockTemplate template) {
        super(template);
    }

    @Override
    public CustomBlockTemplate getInstance() throws ClassNotFoundException {
        return ItemMods.getPlugin().getApi().getBlockTemplate(getName());
    }
}
