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
        ItemMods.getMainConfig().getItems().stream().filter(itemAsset -> Objects.requireNonNull(anvilInventory.getItem(0)).isSimilar(itemAsset.getItemStack()) && !itemAsset.isCanRename()).forEach(itemAsset -> {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(ItemMods.getTranslationConfig().getTranslation("event.rename"));
        });
    }
}
