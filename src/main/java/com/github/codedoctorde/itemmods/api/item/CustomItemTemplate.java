package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.config.ItemConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author CodeDoctorDE
 */
public interface CustomItemTemplate {

    ItemStack getIcon(ItemConfig itemConfig);

    ItemStack getMainIcon(ItemConfig itemConfig);

    boolean isCompatible(ItemConfig itemConfig);

    void openConfig(ItemConfig itemConfig, Player player);

    String getName();
}
