package com.github.codedoctorde.itemmods.listener;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.addon.templates.item.BlockSetTemplate;
import com.github.codedoctorde.itemmods.api.block.CustomBlock;
import com.github.codedoctorde.itemmods.api.item.CustomItem;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomBlockListener implements Listener {

    @EventHandler
    public void onCustomBlockPlaced(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        CustomItem customItem = new CustomItem(event.getItem());
        if (customItem.getConfig() == null)
            return;
        if (!(customItem.getConfig().getTemplate() instanceof BlockSetTemplate))
            return;
        BlockSetTemplate template = (BlockSetTemplate) customItem.getConfig().getTemplate();
        if (template.getBlock(customItem) == null)
            return;
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
        event.setCancelled(true);
        if (location.distance(event.getPlayer().getLocation()) < 1 || location.distance(event.getPlayer().getEyeLocation()) < 1)
            return;
        if (!ItemMods.getPlugin().getApi().getCustomBlockManager().setCustomBlock(location, template.getBlock(customItem)))
            return;
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.getItem().setAmount(event.getItem().getAmount() - customItem.getConfig().getItemStack().getAmount());
    }

    @EventHandler
    public void onCustomBlockGet(InventoryCreativeEvent event) {
        if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE)
            return;
        Block block = event.getWhoClicked().getTargetBlock(null, 10);
        if (block.getType() == Material.AIR)
            return;
        CustomBlock customBlock = ItemMods.getPlugin().getApi().getCustomBlockManager().getCustomBlock(block);
        if (customBlock == null)
            return;
        if (customBlock.getConfig().getReferenceItemConfig() == null)
            return;
        event.getWhoClicked().getInventory().setItemInMainHand(customBlock.getConfig().getReferenceItemConfig().getItemStack());
    }

    @EventHandler
    public void onCustomBlockBreak(BlockBreakEvent event) {
        CustomBlock customBlock = ItemMods.getPlugin().getApi().getCustomBlockManager().getCustomBlock(event.getBlock());
        if (customBlock == null)
            return;
        event.setCancelled(true);
        boolean finished;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
            finished = customBlock.breakBlock(CustomBlock.BlockDropType.NOTHING);
        else if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))
            finished = customBlock.breakBlock(CustomBlock.BlockDropType.SILK_TOUCH);
        else if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LUCK))
            finished = customBlock.breakBlock(CustomBlock.BlockDropType.FORTUNE);
        else
            finished = customBlock.breakBlock(CustomBlock.BlockDropType.DROP);
        if (!finished)
            return;
        event.getBlock().setType(Material.AIR);
        ItemMeta itemMeta = event.getPlayer().getInventory().getItemInMainHand().getItemMeta();
        if (itemMeta instanceof Damageable && event.getPlayer().getGameMode() != GameMode.CREATIVE)
            ((Damageable) itemMeta).setDamage(((Damageable) itemMeta).getDamage());
    }

    @EventHandler
    public void onCustomBlockMove(BlockFromToEvent event) {
        CustomBlock customBlock = ItemMods.getPlugin().getApi().getCustomBlockManager().getCustomBlock(event.getBlock());
        if (customBlock == null)
            return;
        if (customBlock.getArmorStand() == null)
            return;
        customBlock.getArmorStand().teleport(event.getToBlock().getLocation());
    }

    @EventHandler
    public void onCustomBlockPistonRetract(BlockPistonRetractEvent event) {
        for (Block block :
                event.getBlocks()) {
            CustomBlock customBlock = ItemMods.getPlugin().getApi().getCustomBlockManager().getCustomBlock(block);
            if (customBlock == null)
                return;
            if (!customBlock.getConfig().isMoving())
                event.setCancelled(true);
            else if (customBlock.getArmorStand() != null)
                customBlock.getArmorStand().teleport(block.getLocation().add(event.getDirection().getDirection()));
        }
    }

    @EventHandler
    public void onCustomBlockPistonExtend(BlockPistonExtendEvent event) {
        for (Block block :
                event.getBlocks()) {
            CustomBlock customBlock = ItemMods.getPlugin().getApi().getCustomBlockManager().getCustomBlock(block);
            if (customBlock == null)
                return;
            if (!customBlock.getConfig().isMoving())
                event.setCancelled(true);
            else if (customBlock.getArmorStand() != null)
                customBlock.getArmorStand().teleport(block.getLocation().add(event.getDirection().getDirection()));
        }
    }

    @EventHandler
    public void onCustomBlockFall(EntityChangeBlockEvent event) {
        CustomBlock customBlock = ItemMods.getPlugin().getApi().getCustomBlockManager().getCustomBlock(event.getBlock());
        if (customBlock != null)
            event.setCancelled(true);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {

    }

    @EventHandler
    public void onCustomBlockEntityClick(PlayerInteractAtEntityEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Location location = event.getRightClicked().getLocation().clone().add(-0.5, 0, -0.5);
        CustomItem customItem = new CustomItem(item);
        if (customItem.getConfig() == null || customItem.getConfig().getTemplate() == null || !(customItem.getConfig().getTemplate() instanceof BlockSetTemplate))
            return;
        BlockSetTemplate template = (BlockSetTemplate) customItem.getConfig().getTemplate();
        if (template.getBlock(customItem) == null || item.getAmount() < customItem.getConfig().getItemStack().getAmount())
            return;
        if (ItemMods.getPlugin().getApi().getCustomBlockManager().getCustomBlock(location) == null)
            return;
        event.setCancelled(true);
        if (location.distance(event.getPlayer().getLocation()) < 1 || location.distance(event.getPlayer().getEyeLocation()) < 1)
            return;
        if (event.getPlayer().getLocation().distance(location.clone().add(0, 1, 0)) < 1)
            return;
        if (!ItemMods.getPlugin().getApi().getCustomBlockManager().setCustomBlock(location.clone().add(0, 1, 0), template.getBlock(customItem)))
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
        CustomBlock customBlock = ItemMods.getPlugin().getApi().getCustomBlockManager().getCustomBlock(location);
        if (customBlock == null)
            return;
        event.setCancelled(true);
        if (((Player) event.getDamager()).getGameMode() == GameMode.CREATIVE)
            customBlock.breakBlock(CustomBlock.BlockDropType.NOTHING);
        else if (((Player) event.getEntity()).getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))
            customBlock.breakBlock(CustomBlock.BlockDropType.SILK_TOUCH);
        else
            customBlock.breakBlock(CustomBlock.BlockDropType.DROP);
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