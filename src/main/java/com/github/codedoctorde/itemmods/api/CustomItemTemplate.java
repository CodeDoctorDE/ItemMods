package com.github.codedoctorde.itemmods.api;

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

    void load(String data, Player player, CustomItem customItem);

    String save(CustomItem customItem);

    void openConfig(ItemConfig itemConfig, Player player);

    String getName();

    /**
     * not used yet
     *
     * @param block
     */
    void onEnable(CustomBlock block);

    /**
     * not used yet
     *
     * @param block
     */
    void onDisable(CustomBlock block);
}
