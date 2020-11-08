package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.api.item.StaticCustomItem;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * @author CodeDoctorDE
 */
public abstract class InteractiveStaticCustomBlock extends StaticCustomBlock implements Listener {
    protected ItemModsAddon addon;

    public InteractiveStaticCustomBlock(ItemModsAddon addon, String name) {
        super(addon, name);
        this.addon = addon;
    }

    @Override
    public void onLoad() {
        Bukkit.getPluginManager().registerEvents(this, addon.getPlugin());
    }

    @Override
    public void onUnload() {
        HandlerList.unregisterAll(this);
    }
}
