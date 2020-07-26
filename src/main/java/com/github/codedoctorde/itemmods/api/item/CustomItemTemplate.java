package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.gitlab.codedoctorde.api.ui.Gui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public interface CustomItemTemplate {

    ItemStack getIcon(ItemConfig itemConfig);

    ItemStack getMainIcon(ItemConfig itemConfig);

    boolean isCompatible(ItemConfig itemConfig);

    boolean openConfigGui(ItemConfig itemConfig, Player player);

    String getName();
}
