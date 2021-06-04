package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.CustomTemplateData;

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
    public CustomBlockTemplate getInstance() {
        return ItemMods.getApi().getBlockTemplate(getName());
    }
}
