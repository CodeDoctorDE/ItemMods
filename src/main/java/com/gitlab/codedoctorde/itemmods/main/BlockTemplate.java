package com.gitlab.codedoctorde.itemmods.main;

import com.gitlab.codedoctorde.itemmods.config.BlockDataConfig;
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

    public abstract void load(JsonElement data, Location location, BlockDataConfig blockConfig);

    public abstract JsonElement save(Location location, BlockDataConfig blockConfig);

    public abstract void onBlockBreak(BlockDataConfig blockConfig, BlockBreakEvent event);

    public abstract void onBlockPlace(BlockDataConfig blockConfig, BlockPlaceEvent event);

    public abstract void onBlockInteract(BlockDataConfig blockConfig, PlayerInteractEvent event);

}
