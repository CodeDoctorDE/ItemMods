package com.gitlab.codedoctorde.itemmods.listener;

import com.gitlab.codedoctorde.itemmods.main.Main;
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
        Main.getPlugin().getMainConfig().getItems().stream().filter(itemConfig -> Objects.requireNonNull(anvilInventory.getItem(0)).isSimilar(itemConfig.getItemStack()) && !itemConfig.isCanRename()).forEach(itemConfig -> {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(Main.getPlugin().getTranslationConfig().getString("event", "rename"));
        });
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        event.getWhoClicked().sendMessage("test");
    }
}
