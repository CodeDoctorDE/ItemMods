package com.github.codedoctorde.itemmods.listener;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.api.CustomBlock;
import com.github.codedoctorde.itemmods.api.CustomItem;
import com.github.codedoctorde.itemmods.templates.item.BlockSetTemplate;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CustomBlockListener implements Listener {

    @EventHandler
    public void onCustomBlockPlaced(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        CustomItem customItem = new CustomItem(event.getItem());
        if (customItem.getConfig() == null)
            return;
        event.setCancelled(true);
        if (!(customItem.getConfig().getTemplate() instanceof BlockSetTemplate))
            return;
        BlockSetTemplate template = (BlockSetTemplate) customItem.getConfig().getTemplate();
        if (event.getItem().getAmount() < customItem.getConfig().getItemStack().getAmount()) return;
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
        if (location.distance(event.getPlayer().getLocation()) < 1 || location.distance(event.getPlayer().getEyeLocation()) < 1)
            return;
        if (!Main.getPlugin().getApi().getCustomBlockManager().setCustomBlock(location, template.getBlock(customItem)))
            return;
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.getItem().setAmount(event.getItem().getAmount() - customItem.getConfig().getItemStack().getAmount());
    }

    @EventHandler
    public void onCustomBlockBreak(BlockBreakEvent event) {
        CustomBlock customBlock = Main.getPlugin().getApi().getCustomBlockManager().getCustomBlock(event.getBlock());
        if (customBlock == null)
            return;
        event.getBlock().setType(Material.AIR);
        event.setCancelled(true);
        customBlock.breakBlock(event.getPlayer().getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void onCustomBlockMove(BlockFromToEvent event) {
        CustomBlock customBlock = Main.getPlugin().getApi().getCustomBlockManager().getCustomBlock(event.getBlock());
        if (customBlock == null)
            return;
        customBlock.getArmorStand().teleport(event.getToBlock().getLocation());
    }

    @EventHandler
    public void onCustomBlockPistonRetract(BlockPistonRetractEvent event) {
        for (Block block :
                event.getBlocks()) {
            CustomBlock customBlock = Main.getPlugin().getApi().getCustomBlockManager().getCustomBlock(block);
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
            CustomBlock customBlock = Main.getPlugin().getApi().getCustomBlockManager().getCustomBlock(block);
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
        CustomBlock customBlock = Main.getPlugin().getApi().getCustomBlockManager().getCustomBlock(event.getBlock());
        if (customBlock != null)
            event.setCancelled(true);
    }

    @EventHandler
    public void onCustomBlockEntityClick(PlayerInteractAtEntityEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Location location = event.getRightClicked().getLocation().clone().add(-0.5, 0, -0.5);
        CustomItem customItem = new CustomItem(item);
        if (customItem.getConfig() == null)
            return;
        if (!(customItem.getConfig().getTemplate() instanceof BlockSetTemplate))
            return;
        BlockSetTemplate template = (BlockSetTemplate) customItem.getConfig().getTemplate();
        if (item.getAmount() < customItem.getConfig().getItemStack().getAmount()) return;
        event.setCancelled(true);
        if (Main.getPlugin().getApi().getCustomBlockManager().getCustomBlock(location) == null)
            return;
        if (location.distance(event.getPlayer().getLocation()) < 1 || location.distance(event.getPlayer().getEyeLocation()) < 1)
            return;
        if (event.getPlayer().getLocation().distance(location.clone().add(0, 1, 0)) < 1)
            return;
        if (!Main.getPlugin().getApi().getCustomBlockManager().setCustomBlock(location.clone().add(0, 1, 0), template.getBlock(customItem)))
            return;
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            item.setAmount(item.getAmount() - customItem.getConfig().getItemStack().getAmount());
    }

    @EventHandler
    public void onCustomBlockEntityHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof ArmorStand))
            return;
        Location location = event.getEntity().getLocation().clone().add(-0.5, 0, -0.5);
        CustomBlock customBlock = Main.getPlugin().getApi().getCustomBlockManager().getCustomBlock(location);
        if (customBlock == null)
            return;
        event.setCancelled(true);
        customBlock.breakBlock(((Player) event.getDamager()).getGameMode() != GameMode.CREATIVE);
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