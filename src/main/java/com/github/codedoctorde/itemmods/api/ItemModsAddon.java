package com.github.codedoctorde.itemmods.api;

import com.gitlab.codedoctorde.api.ui.Gui;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author CodeDoctorDE
 */
public interface ItemModsAddon {
    List<CustomItemTemplate> getItemTemplates();

    List<CustomBlockTemplate> getBlockTemplates();

    String getName();

    ItemStack getIcon();

    Gui openConfig();
}
