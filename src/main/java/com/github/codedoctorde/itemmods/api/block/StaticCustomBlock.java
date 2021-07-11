package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.api.ItemModsAddon;

/**
 * @author CodeDoctorDE
 */
public abstract class StaticCustomBlock extends BlockConfig implements CustomBlockTemplate {

    public StaticCustomBlock(ItemModsAddon addon, String name) {
        super(addon.getName(), name);
    }
}
