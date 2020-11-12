package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.api.nms.block.BlockNBT;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.events.CustomBlockPlaceEvent;
import com.github.codedoctorde.itemmods.config.ArmorStandBlockConfig;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomBlockManager {
    private final List<CustomBlock> loadedBlocks = new ArrayList<>();

    public static String locationToString(final Location location) {
        if (location == null) return "";
        return Objects.requireNonNull(location.getWorld()).getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }

    public static Location stringToLocation(final String location) {
        String[] locationArray = location.split(":");
        return new Location(Bukkit.getWorld(locationArray[0]), Double.parseDouble(locationArray[1]),
                Double.parseDouble(locationArray[2]),
                Double.parseDouble(locationArray[3]));
    }

    /**
     * Get the custom block by the location
     *
     * @param location the location of the custom block
     * @return The custom block
     */
    @Nullable
    public CustomBlock getCustomBlock(final Location location) {
        Block block = location.getBlock();
        for (BlockConfig blockConfig : ItemMods.getPlugin().getMainConfig().getBlocks())
            if (blockConfig.getArmorStand() != null) {
                Location entityLocation = location.clone().add(0.5, 0, 0.5);
                List<Entity> entities = new ArrayList<>(Objects.requireNonNull(entityLocation.getWorld()).getNearbyEntities(entityLocation, 0.05, 0.001, 0.05));
                for (Entity entity : entities)
                    if (entity instanceof ArmorStand && entity.getLocation().getY() == location.getY()) {
                        CustomBlock customBlock = getCustomBlock((ArmorStand) entity, blockConfig);
                        if (customBlock != null)
                            return customBlock;
                    }
            } else if (block.getState() instanceof TileState) {
                CustomBlock customBlock = getCustomBlock((TileState) block.getState(), blockConfig);
                if (customBlock != null)
                    return customBlock;
            }
        return null;
    }

    @Nullable
    public CustomBlock getCustomBlock(final ArmorStand entity, BlockConfig blockConfig) {
        return getCustomBlock(entity.getPersistentDataContainer(), entity, blockConfig, entity.getLocation());
    }

    @Nullable
    public CustomBlock getCustomBlock(final TileState tileState, BlockConfig blockConfig) {
        return getCustomBlock(tileState.getPersistentDataContainer(), null, blockConfig, tileState.getLocation());
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

    @Nullable
    public CustomBlock getCustomBlock(final ArmorStand entity, final Location location) {
        CustomBlock customBlock = new CustomBlock(entity);
        return customBlock.getConfig() != null ? customBlock:null;
    }

    public List<BlockConfig> getBlocks() {
        return ItemMods.getPlugin().getMainConfig().getBlocks();
    }

    /**
     * @param location    The location where the custom block will be placed!
     * @param blockConfig The block config for the custom block
     * @param player      The player who is placing the block
     * @return Returns if it was placed!
     */
    public boolean setCustomBlock(Location location, BlockConfig blockConfig, Player player) {
        if (getCustomBlock(location) != null)
            return false;
        if (location.getBlock().getType().isSolid())
            return false;
        CustomBlockPlaceEvent event = new CustomBlockPlaceEvent(location, blockConfig, player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return false;
        Block block = Objects.requireNonNull(location.getWorld()).getBlockAt(location);
        if (blockConfig.getBlock() != null)
            block.setBlockData(blockConfig.getBlock());
        ArmorStand armorStand = null;

        ArmorStandBlockConfig armorStandBlockConfig = blockConfig.getArmorStand();
        if (armorStandBlockConfig != null) armorStand = armorStandBlockConfig.spawn(location);
        if (blockConfig.getNbt() != null)
            BlockNBT.setNbt(block, blockConfig.getNbt());
        if (location.getChunk().isLoaded())
            loadedBlocks.add(new CustomBlock(location, armorStand));
        return true;
    }

    public void onTick() {
        loadedBlocks.stream().filter(customBlock -> customBlock.getConfig().getTemplate() != null).forEach(customBlock -> {
            CustomBlockTemplate template = customBlock.getConfig().getTemplate().getInstance();
            if (template != null)
                template.tick(customBlock);
        });
    }

    public List<CustomBlock> getLoadedBlocks() {
        return loadedBlocks;
    }
}
