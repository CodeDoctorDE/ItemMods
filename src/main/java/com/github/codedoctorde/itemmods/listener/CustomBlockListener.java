package com.github.codedoctorde.itemmods.listener;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.api.CustomBlock;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public class CustomBlockListener implements Listener {

    @EventHandler
    public void onCustomBlockPlaced(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        for (BlockConfig block :
                Main.getPlugin().getMainConfig().getBlocks()) {
            if (event.getItem().isSimilar(block.getItemStack())) {
                event.setCancelled(true);
                if (event.getItem().getAmount() < block.getItemStack().getAmount()) return;
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
                if (!Main.getPlugin().getCustomBlockManager().setCustomBlock(location, block))
                    return;
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
                    event.getItem().setAmount(event.getItem().getAmount() - block.getItemStack().getAmount());
            }
        }
    }

    @EventHandler
    public void onCustomBlockBreak(BlockBreakEvent event) {
        CustomBlock customBlock = Main.getPlugin().getCustomBlockManager().getCustomBlock(event.getBlock());
        if (customBlock == null)
            return;
        event.getBlock().setType(Material.AIR);
        event.setCancelled(true);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE && (!customBlock.getConfig().isDrop()) || (customBlock.getConfig().isDrop() && event.isDropItems())) {
            event.getBlock().getDrops().clear();
            customBlock.getConfig().getDrops().stream().filter(drop -> new Random().nextInt(99) + 1 <= drop.getRarity()).forEach(drop -> event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), drop.getItemStack()));
        }
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
            if (!customBlock.getConfig().isMoving())
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
            if (!customBlock.getConfig().isMoving())
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

    @EventHandler
    public void onCustomBlockRedstone(BlockRedstoneEvent event) {
       /* for (int x = -1; x < 2; x++)
            for (int y = -1; y < 2; y++)
                for (int z = -1; z < 2; z++)
                    if (event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().getBlockX() + x,
                            event.getBlock().getLocation().getBlockY() + y, event.getBlock().getLocation().getBlockZ() + z).equals()) {
                    }*/
    }
}