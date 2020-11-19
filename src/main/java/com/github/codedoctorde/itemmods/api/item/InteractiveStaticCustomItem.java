package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * @author CodeDoctorDE
 */
public abstract class InteractiveStaticCustomItem extends StaticCustomItem implements Listener {
    protected ItemModsAddon addon;

    public InteractiveStaticCustomItem(ItemModsAddon addon, String name) {
        super(addon, name);
        this.addon = addon;
    }

    @Override
    public void onLoad(Player player) {
        Bukkit.getPluginManager().registerEvents(this, addon.getPlugin());
    }

    @Override
    public void onUnload(Player player) {
        HandlerList.unregisterAll(this);
    }
}
