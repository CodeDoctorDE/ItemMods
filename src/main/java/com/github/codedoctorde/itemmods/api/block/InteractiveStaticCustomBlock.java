package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

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
    public void onLoad(CustomBlock customBlock) {
        Bukkit.getPluginManager().registerEvents(this, addon.getPlugin());
    }

    @Override
    public void onUnload(CustomBlock customBlock) {
        HandlerList.unregisterAll(this);
    }
}
