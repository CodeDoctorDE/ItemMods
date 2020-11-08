package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.config.BlockConfig;

/**
 * @author CodeDoctorDE
 */
public abstract class StaticCustomBlock extends BlockConfig implements CustomBlockTemplate {
    public StaticCustomBlock(ItemModsAddon addon, String name){
        super(name);
    }
}
