package com.gitlab.codedoctorde.itemmods.main;

import com.gitlab.codedoctorde.itemmods.api.CustomBlock;
import com.google.gson.JsonElement;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Coming soon... (available 1.2)
 */
public abstract class BlockTemplate {
    BlockTemplate() {

    }

    public abstract void load(JsonElement data, Location location, CustomBlock blockConfig);

    public abstract JsonElement save(Location location, CustomBlock blockConfig);

    public abstract void onBlockBreak(CustomBlock blockConfig, BlockBreakEvent event);

    public abstract void onBlockPlace(CustomBlock blockConfig, BlockPlaceEvent event);

    public abstract void onBlockInteract(CustomBlock blockConfig, PlayerInteractEvent event);

}
