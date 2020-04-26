package com.github.codedoctorde.itemmods.api;

import com.gitlab.codedoctorde.api.ui.Gui;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author CodeDoctorDE
 */
public interface ItemModsAddon {
    @NotNull
    List<CustomItemTemplate> getItemTemplates();

    @NotNull
    List<CustomBlockTemplate> getBlockTemplates();

    @NotNull
    String getName();

    @NotNull
    ItemStack getIcon();

    @Nullable
    Gui openConfig();
}
