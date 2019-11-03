package eu.vangora.itemmods.listener;

import eu.vangora.itemmods.config.ItemConfig;
import eu.vangora.itemmods.main.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;

import java.util.Objects;

public class ItemListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!(event.getClickedInventory() instanceof AnvilInventory)) return;
        AnvilInventory anvilInventory = ((AnvilInventory) event.getClickedInventory());
        if(event.getSlot() < 2) return;
        if(anvilInventory.getItem(0) == null || anvilInventory.getRenameText() == null)return;
        Main.getPlugin().getMainConfig().getItems().stream().filter(itemConfig -> Objects.requireNonNull(anvilInventory.getItem(0)).isSimilar(itemConfig.getItemStack()) && !itemConfig.isCanRename()).forEach(itemConfig -> {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(Main.getPlugin().getTranslationConfig().getString("event", "rename"));
        });
    }
}
