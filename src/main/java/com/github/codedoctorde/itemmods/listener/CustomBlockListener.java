package com.github.codedoctorde.itemmods.listener;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.addon.templates.item.BlockSetTemplate;
import com.github.codedoctorde.itemmods.api.block.CustomBlock;
import com.github.codedoctorde.itemmods.api.item.CustomItem;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomBlockListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCustomBlockPlaced(PlayerInteractEvent event) {
        if (event.getItem() == null || event.useItemInHand() == Event.Result.DENY ||
                event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                event.getClickedBlock() == null)
            return;
        CustomItem customItem = new CustomItem(event.getItem());
        if (customItem.getConfig() == null)
            return;
        customItem.getConfig().getCustomTemplates().forEach(customTemplateData -> {
            if (customTemplateData.getObject().getTemplate() instanceof BlockSetTemplate) {
                var template = (BlockSetTemplate) customTemplateData.getObject().getTemplate();
                if (template.getBlock(customTemplateData) == null)
                    return;
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
                if (!ItemMods.getCustomBlockManager().setCustomBlock(location, template.getBlock(customTemplateData), player))
                    return;
                event.setCancelled(true);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
                    event.getItem().setAmount(event.getItem().getAmount() - 1);
            }
        });
    }

    /*@EventHandler
    public void onCustomBlockGet(InventoryCreativeEvent event) {
        if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE)
            return;
        Block block = event.getWhoClicked().getTargetBlock(null, 10);
        if (block.getType() == Material.AIR)
            return;
        CustomBlock customBlock = new CustomBlock(block);
        if (customBlock.getConfig() == null)
            return;
        event.getWhoClicked().getInventory().setItemInMainHand(customBlock.getConfig().getReferenceItemAsset().getItemStack());
    }*/

    @EventHandler(priority = EventPriority.HIGH)
    public void onCustomBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;
        CustomBlock customBlock = new CustomBlock(event.getBlock());
        if (customBlock.getConfig() == null)
            return;
        event.setCancelled(true);
        if (!customBlock.breakBlock(event.getPlayer()))
            return;
        event.getBlock().setType(Material.AIR);
        ItemMeta itemMeta = event.getPlayer().getInventory().getItemInMainHand().getItemMeta();
        if (itemMeta instanceof Damageable && event.getPlayer().getGameMode() != GameMode.CREATIVE)
            ((Damageable) itemMeta).setDamage(((Damageable) itemMeta).getDamage());
    }

    @EventHandler
    public void onCustomBlockFall(EntityChangeBlockEvent event) {
        CustomBlock customBlock = new CustomBlock(event.getBlock());
        if (customBlock.getConfig() != null)
            event.setCancelled(true);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {

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