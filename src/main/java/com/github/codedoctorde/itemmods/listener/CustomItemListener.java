package com.github.codedoctorde.itemmods.listener;

import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;

import java.util.Objects;

public class CustomItemListener implements Listener {


    @EventHandler
    public void onAnvilRename(InventoryClickEvent event) {
        if (!(event.getClickedInventory() instanceof AnvilInventory)) return;
        AnvilInventory anvilInventory = ((AnvilInventory) event.getClickedInventory());
        if (event.getSlot() < 2) return;
        if (anvilInventory.getItem(0) == null || anvilInventory.getRenameText() == null) return;
        ItemMods.getPlugin().getMainConfig().getItems().stream().filter(itemConfig -> Objects.requireNonNull(anvilInventory.getItem(0)).isSimilar(itemConfig.getItemStack()) && !itemConfig.isCanRename()).forEach(itemConfig -> {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(ItemMods.getPlugin().getTranslationConfig().getTranslation("event.rename"));
        });
    }
}
