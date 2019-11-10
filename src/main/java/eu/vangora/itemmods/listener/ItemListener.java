package eu.vangora.itemmods.listener;

import eu.vangora.itemmods.config.BlockConfig;
import eu.vangora.itemmods.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EntityEquipment;

import java.util.ArrayList;
import java.util.List;
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
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        for (BlockConfig block :
                Main.getPlugin().getMainConfig().getBlocks()) {
            if (event.getItem().isSimilar(block.getItemStack())) {
                if (event.getItem().getAmount() < block.getItemStack().getAmount()) return;
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
                if (location.getNearbyPlayers(1).contains(event.getPlayer())) return;
                else event.getPlayer().sendMessage("no");
                if (!location.getBlock().isEmpty() || location.getBlock().getState().equals(block.getBlock())) return;
                location.getWorld().getBlockAt(location).setBlockData(block.getBlock());
                ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
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
                armorStand.setSmall(block.isSmall());
                armorStand.setMarker(block.isMarker());
                armorStand.setInvulnerable(block.isInvulnerable());
                armorStand.setCustomNameVisible(block.isCustomNameVisible());
                armorStand.setCustomName(block.getCustomName());
                armorStand.setVisible(!block.isInvisible());
                armorStand.getScoreboardTags().add(block.getTag());
                armorStand.setGravity(false);
                event.getItem().setAmount(event.getItem().getAmount() - block.getItemStack().getAmount());
            }
        }
    }

    @EventHandler
    public void onCustomBlockBreak(BlockBreakEvent event) {
        List<Entity> entities = new ArrayList<>(event.getBlock().getLocation().add(0.5, 0, 0.5).getNearbyLivingEntities(0.25));
        Bukkit.broadcastMessage(entities.toString());
        if (entities.size() < 1) return;
        Main.getPlugin().getMainConfig().getBlocks().forEach(block -> entities.stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND).map(entity -> (ArmorStand) entity).filter(armorStand -> armorStand.getScoreboardTags().contains(block.getTag())).forEach(armorStand -> {
            Bukkit.broadcastMessage("test");
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), block.getItemStack());
            armorStand.remove();
        }));
    }
}
