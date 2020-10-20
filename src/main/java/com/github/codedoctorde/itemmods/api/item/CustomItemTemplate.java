package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.config.ItemConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author CodeDoctorDE
 */
public interface CustomItemTemplate {

    @NotNull
    ItemStack getIcon(ItemConfig itemConfig);

    @NotNull
    ItemStack getMainIcon(ItemConfig itemConfig);

    boolean isCompatible(ItemConfig itemConfig);

    boolean openConfigGui(ItemConfig itemConfig, Player player);

    @NotNull
    String getName();
}
