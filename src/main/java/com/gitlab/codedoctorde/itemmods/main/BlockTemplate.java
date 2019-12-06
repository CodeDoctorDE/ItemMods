package com.gitlab.codedoctorde.itemmods.main;

import com.gitlab.codedoctorde.itemmods.config.BlockConfig;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Coming soon... (available 1.2)
 */
public interface BlockTemplate {
    void onBlockBreak(BlockConfig blockConfig, BlockBreakEvent event);

    void onBlockPlace(BlockConfig blockConfig, BlockPlaceEvent event);

    void onBlockInteract(BlockConfig blockConfig, PlayerInteractEvent event);
}
