package com.gitlab.codedoctorde.itemmods.api;

import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.itemmods.config.BlockConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author CodeDoctorDE
 */
public interface CustomItemTemplate {

    GuiItem getGuiItem(BlockConfig blockConfig);

    void load(String data, Player player, CustomItem blockConfig);

    String save(Location location, CustomBlock blockConfig);

    void onBlockBreak(CustomBlock blockConfig, BlockBreakEvent event);

    void onBlockPlace(CustomBlock blockConfig, BlockPlaceEvent event);

    void onBlockInteract(CustomBlock blockConfig, PlayerInteractEvent event);
}
