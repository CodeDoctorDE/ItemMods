package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.pack.template.item.CustomItemTemplate;

/**
 * @author CodeDoctorDE
 */
public abstract class StaticCustomItem extends ItemAsset implements CustomItemTemplate {
    private final ItemModsAddon addon;

    public StaticCustomItem(ItemModsAddon addon, String name) {
        super(addon.getName(), name);
        this.addon = addon;
    }

    public ItemModsAddon getAddon() {
        return addon;
    }
}
