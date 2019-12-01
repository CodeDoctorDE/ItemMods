package com.gitlab.codedoctorde.itemmods.listener;

import com.gitlab.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.itemmods.main.CustomBlock;
import com.gitlab.codedoctorde.itemmods.main.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CustomBlockListener implements Listener {

    @EventHandler
    public void onCustomBlockPlaced(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        for (BlockConfig block :
                Main.getPlugin().getMainConfig().getBlocks()) {
            if (event.getItem().isSimilar(block.getItemStack())) {
                if (event.getItem().getAmount() < block.getItemStack().getAmount()) return;
                event.setCancelled(true);
                Player player = event.getPlayer();
                Location location = event.getClickedBlock().getLocation();
                switch (event.getBlockFace()) {
                    case UP:
                        location.add(0, 1, 0);
                        break;
                    case DOWN:
                        location.add(0, -1, 0);
                        break;
                    case EAST:
                        location.add(1, 0, 0);
                        break;
                    case WEST:
                        location.add(-1, 0, 0);
                        break;
                    case NORTH:
                        location.add(0, 0, -1);
                        break;
                    case SOUTH:
                        location.add(0, 0, 1);
                        break;
                }
                if (Main.getPlugin().getCustomBlockManager().setCustomBlock(location, block))
                    return;
                event.getItem().setAmount(event.getItem().getAmount() - block.getItemStack().getAmount());
            }
        }
    }

    @EventHandler
    public void onCustomBlockBreak(BlockBreakEvent event) {
        CustomBlock customBlock = Main.getPlugin().getCustomBlockManager().getCustomBlock(event.getBlock());
        if (customBlock == null)
            return;
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), customBlock.getConfig().getItemStack());
        customBlock.getArmorStand().remove();
    }

    @EventHandler
    public void onCustomBlockMove(BlockFromToEvent event) {
        CustomBlock customBlock = Main.getPlugin().getCustomBlockManager().getCustomBlock(event.getBlock());
        if (customBlock == null)
            return;
        customBlock.getArmorStand().teleport(event.getToBlock().getLocation());
    }

    @EventHandler
    public void onCustomBlockPistonRetract(BlockPistonRetractEvent event) {
        for (Block block :
                event.getBlocks()) {
            CustomBlock customBlock = Main.getPlugin().getCustomBlockManager().getCustomBlock(block);
            if (customBlock == null)
                return;
            if (!customBlock.getConfig().isMove())
                event.setCancelled(true);
            else
                customBlock.getArmorStand().teleport(block.getLocation().add(event.getDirection().getDirection()));
        }
    }

    @EventHandler
    public void onCustomBlockPistonExtend(BlockPistonExtendEvent event) {
        for (Block block :
                event.getBlocks()) {
            CustomBlock customBlock = Main.getPlugin().getCustomBlockManager().getCustomBlock(block);
            if (customBlock == null)
                return;
            if (!customBlock.getConfig().isMove())
                event.setCancelled(true);
            else
                customBlock.getArmorStand().teleport(block.getLocation().add(event.getDirection().getDirection()));
        }
    }

    @EventHandler
    public void onCustomBlockFall(EntityChangeBlockEvent event) {
        CustomBlock customBlock = Main.getPlugin().getCustomBlockManager().getCustomBlock(event.getBlock());
        if (customBlock != null)
            event.setCancelled(true);
    }
}
