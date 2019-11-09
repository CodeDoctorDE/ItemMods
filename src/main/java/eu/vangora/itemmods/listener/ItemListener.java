package eu.vangora.itemmods.listener;

import eu.vangora.itemmods.config.BlockConfig;
import eu.vangora.itemmods.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;

import java.util.Objects;

public class ItemListener implements Listener {



    @EventHandler
    public void onAnvilRename(InventoryClickEvent event){
        if(!(event.getClickedInventory() instanceof AnvilInventory)) return;
        AnvilInventory anvilInventory = ((AnvilInventory) event.getClickedInventory());
        if(event.getSlot() < 2) return;
        if(anvilInventory.getItem(0) == null || anvilInventory.getRenameText() == null)return;
        Main.getPlugin().getMainConfig().getItems().stream().filter(itemConfig -> Objects.requireNonNull(anvilInventory.getItem(0)).isSimilar(itemConfig.getItemStack()) && !itemConfig.isCanRename()).forEach(itemConfig -> {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(Main.getPlugin().getTranslationConfig().getString("event", "rename"));
        });
    }

    @EventHandler
    public void onCustomBlockPlaced(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        for (BlockConfig block :
                Main.getPlugin().getMainConfig().getBlocks()) {
            if (event.getItem().isSimilar(block.getItemStack())) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                player.getEyeLocation().getWorld().getBlockAt(player.getEyeLocation()).setBlockData(block.getBlock());
            }
        }
    }

}
