package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.config.ArmorStandBlockConfig;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.api.config.database.BlobConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomBlockManager {
    private List<BlockConfig> blockConfigs;
    private BlobConfig blockDataConfig;

    public CustomBlockManager(List<BlockConfig> blockConfigs) {
        this.blockConfigs = blockConfigs;
    }

    /**
     * Get the custom block by the location
     *
     * @param location the location of the custom block
     * @return The custom block
     */
    @Nullable
    public CustomBlock getCustomBlock(final Location location) {
        Location entityLocation = location.clone().add(0.5, 0, 0.5);
        List<Entity> entities = new ArrayList<>(Objects.requireNonNull(entityLocation.getWorld()).getNearbyEntities(entityLocation, 0.01, 0.001, 0.01));
        for (BlockConfig block : Main.getPlugin().getMainConfig().getBlocks())
            for (Entity entity : entities)
                if (entity.getType() == EntityType.ARMOR_STAND) if (entity.getScoreboardTags().contains(block.getTag()))
                    if (entityLocation.distance(entity.getLocation()) <= 0.5)
                        return new CustomBlock(block, location, (ArmorStand) entity);
        return null;
    }

    /**
     * Get the custom block by the block
     *
     * @param block the "real" block of the custom block
     * @return The custom block
     */
    @Nullable
    public CustomBlock getCustomBlock(final Block block) {
        return getCustomBlock(block.getLocation());
    }

    public List<BlockConfig> getBlockConfigs() {
        return blockConfigs;
    }

    public static Location stringToLocation(final String location) {
        String[] locationArray = location.split(":");
        return new Location(Bukkit.getWorld(locationArray[0]), Double.parseDouble(locationArray[1]),
                Double.parseDouble(locationArray[2]),
                Double.parseDouble(locationArray[3]));
    }

    public static String locationToString(final Location location) {
        if (location == null) return "";
        return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }

    /**
     * @param location    The location where the custom block will be placed!
     * @param blockConfig The block config for the custom block
     * @return Returns if it was placed!
     */
    public boolean setCustomBlock(Location location, BlockConfig blockConfig) {
        if (Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, 0.25, 1.1, 0.25).size() > 0)
            return false;
        if (!location.getBlock().isEmpty() || location.getBlock().getState().equals(blockConfig.getBlock()))
            return false;
        Block block = location.getWorld().getBlockAt(location);
        block.setBlockData(blockConfig.getBlock());
        ArmorStand armorStand = null;

        ArmorStandBlockConfig armorStandBlockConfig = blockConfig.getArmorStand();
        if (armorStandBlockConfig != null) {
            armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
            if (armorStand.getEquipment() == null)
                return false;
            EntityEquipment equipment = armorStand.getEquipment();
            armorStand.setBasePlate(armorStandBlockConfig.isBasePlate());
            equipment.setHelmet(armorStandBlockConfig.getHelmet());
            equipment.setChestplate(armorStandBlockConfig.getChestplate());
            equipment.setLeggings(armorStandBlockConfig.getLeggings());
            equipment.setBoots(armorStandBlockConfig.getBoots());

            equipment.setItemInMainHand(armorStandBlockConfig.getMainHand());
            equipment.setItemInOffHand(armorStandBlockConfig.getOffHand());
            armorStand.setSmall(armorStandBlockConfig.isSmall());
            armorStand.setMarker(armorStandBlockConfig.isMarker());
            armorStand.setInvulnerable(armorStandBlockConfig.isInvulnerable());
            armorStand.setCustomNameVisible(armorStandBlockConfig.isCustomNameVisible());
            armorStand.setCustomName(armorStandBlockConfig.getCustomName());
            armorStand.setVisible(!armorStandBlockConfig.isInvisible());
            armorStand.getScoreboardTags().add(blockConfig.getTag());
            armorStand.setGravity(false);
        }
        CustomBlock customBlock = new CustomBlock(blockConfig, location, armorStand);
        customBlock.configure();

        return true;
    }

}
