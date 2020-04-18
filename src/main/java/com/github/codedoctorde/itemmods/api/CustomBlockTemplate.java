package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.google.gson.JsonElement;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author CodeDoctorDE
 */
public interface CustomBlockTemplate {

    GuiItem getGuiItem(BlockConfig blockConfig);

    void load(JsonElement data, Location location, CustomBlock blockConfig);

    JsonElement save(Location location, CustomBlock blockConfig);

    void onBlockBreak(CustomBlock blockConfig, BlockBreakEvent event);

    void onBlockPlace(CustomBlock blockConfig, BlockPlaceEvent event);

    void onBlockInteract(CustomBlock blockConfig, PlayerInteractEvent event);
}
