package eu.vangora.itemmods.listener;

import eu.vangora.itemmods.config.BlockConfig;
import eu.vangora.itemmods.main.Main;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EntityEquipment;

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
                Location location = player.getTargetBlock(null, 6).getLocation().add(0, 1, 0);
                if (!location.getWorld().getBlockAt(location).isEmpty())
                    return;
                location.getWorld().getBlockAt(location).setBlockData(block.getBlock());
                ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
                if (armorStand.getEquipment() == null)
                    return;
                EntityEquipment equipment = armorStand.getEquipment();
                armorStand.setBasePlate(block.isBasePlate());
                equipment.setHelmet(block.getHelmet());
                equipment.setChestplate(block.getChestplate());
                equipment.setLeggings(block.getLeggings());
                equipment.setBoots(block.getBoots());
                equipment.setItemInMainHand(block.getMainHand());
                equipment.setItemInOffHand(block.getOffHand());
            }
        }
    }

}
