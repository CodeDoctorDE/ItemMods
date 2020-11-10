package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import org.bukkit.NamespacedKey;

import java.awt.image.BufferedImage;

/**
 * @author CodeDoctorDE
 */
public abstract class StaticCustomBlock extends BlockConfig implements CustomBlockTemplate {

    public StaticCustomBlock(ItemModsAddon addon, String name){
        super(addon.getName(), name);
    }
}
