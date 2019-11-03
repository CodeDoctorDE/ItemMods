package eu.vangora.itemmods.listener;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import eu.vangora.itemmods.config.ItemConfig;
import eu.vangora.itemmods.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
    public void onArmorWear(InventoryClickEvent event){
        if(!(event.getWhoClicked().getOpenInventory().getTopInventory() instanceof PlayerInventory) || !event.getClick().isShiftClick() || !(event.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) event.getWhoClicked();

        Main.getPlugin().getMainConfig().getItems().stream().filter(itemConfig -> Objects.requireNonNull(event.getCurrentItem()).isSimilar(itemConfig.getItemStack()) && !itemConfig.isCanRename()).forEach(itemConfig -> {
            event.setCancelled(true);
            switch (itemConfig.getArmorType()){
                case HELMET:
                    if(player.getInventory().getHelmet() == null){
                        player.getInventory().setHelmet(event.getCurrentItem().clone());
                        player.getInventory().remove(event.getCurrentItem());
                    }
                    break;
                case CHESTPLATE:
                    if(player.getInventory().getChestplate() == null){
                        player.getInventory().setChestplate(event.getCurrentItem().clone());
                        player.getInventory().remove(event.getCurrentItem());
                    }
                    break;
                case LEGGINGS:
                    if(player.getInventory().getLeggings() == null){
                        player.getInventory().setLeggings(event.getCurrentItem().clone());
                        player.getInventory().remove(event.getCurrentItem());
                    }
                    break;
                case BOOTS:
                    if(player.getInventory().getBoots() == null){
                        player.getInventory().setBoots(event.getCurrentItem().clone());
                        player.getInventory().remove(event.getCurrentItem());
                    }
                    break;
            }
        });
    }
}
