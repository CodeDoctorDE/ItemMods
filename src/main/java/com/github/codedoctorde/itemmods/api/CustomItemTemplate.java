package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.api.ui.GuiItem;
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

    void onBlockBreak(CustomBlock block, BlockBreakEvent event);

    void onBlockPlace(CustomBlock block, BlockPlaceEvent event);

    void onBlockInteract(CustomBlock block, PlayerInteractEvent event);

    void onEnable(CustomBlock block);

    void onDisable(CustomBlock block);
}
