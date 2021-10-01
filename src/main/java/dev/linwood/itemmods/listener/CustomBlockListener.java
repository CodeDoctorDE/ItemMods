package dev.linwood.itemmods.listener;

import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.addon.templates.item.BlockSetTemplate;
import dev.linwood.itemmods.api.block.CustomBlock;
import dev.linwood.itemmods.api.events.CustomBlockBreakEvent;
import dev.linwood.itemmods.api.item.CustomItem;
import org.bukkit.Bukkit;
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
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class CustomBlockListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCustomBlockPlaced(@NotNull PlayerInteractEvent event) {
        if (event.getItem() == null || event.useItemInHand() == Event.Result.DENY ||
                event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                event.getClickedBlock() == null)
            return;
        CustomItem customItem = new CustomItem(event.getItem());
        if (customItem.getConfig() == null)
            return;
        customItem.getConfig().getCustomTemplates().forEach(customTemplateData -> {
            if (customTemplateData.getTemplate() instanceof BlockSetTemplate) {
                var template = (BlockSetTemplate) customTemplateData.getTemplate();
                if (template.getBlock(customTemplateData) == null)
                    return;
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
                if (location.distance(event.getPlayer().getLocation()) < 1 || location.distance(event.getPlayer().getEyeLocation()) < 1)
                    return;
                if (ItemMods.getCustomBlockManager().create(location, template.getBlock(customTemplateData), player) == null)
                    return;
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
                    event.getItem().setAmount(event.getItem().getAmount() - 1);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCustomBlockBreak(@NotNull BlockBreakEvent event) {
        if (event.isCancelled())
            return;
        CustomBlock customBlock = new CustomBlock(event.getBlock());
        if (customBlock.getConfig() == null)
            return;
        var customEvent = new CustomBlockBreakEvent(customBlock, event.getPlayer());
        Bukkit.getPluginManager().callEvent(customEvent);
        if (!customEvent.isCancelled()) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            event.setExpToDrop(0);
            ItemMeta itemMeta = event.getPlayer().getInventory().getItemInMainHand().getItemMeta();
            if (itemMeta instanceof Damageable && event.getPlayer().getGameMode() != GameMode.CREATIVE)
                ((Damageable) itemMeta).setDamage(((Damageable) itemMeta).getDamage());
        }
    }

    @EventHandler
    public void onCustomBlockFall(@NotNull EntityChangeBlockEvent event) {
        CustomBlock customBlock = new CustomBlock(event.getBlock());
        if (customBlock.getConfig() != null)
            event.setCancelled(true);
    }

}