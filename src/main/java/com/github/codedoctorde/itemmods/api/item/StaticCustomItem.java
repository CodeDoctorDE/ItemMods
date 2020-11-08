package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.config.ItemConfig;

/**
 * @author CodeDoctorDE
 */
public abstract class StaticCustomItem extends ItemConfig implements CustomItemTemplate {
    public StaticCustomItem(String name) {
        super(name);
    }
}
