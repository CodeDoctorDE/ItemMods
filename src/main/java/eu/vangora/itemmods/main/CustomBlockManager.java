package eu.vangora.itemmods.main;

import eu.vangora.itemmods.config.BlockConfig;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomBlockManager {
    private final List<BlockConfig> blockConfigs;

    public CustomBlockManager(List<BlockConfig> blockConfigs) {
        this.blockConfigs = blockConfigs;
    }

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

    @Nullable
    public CustomBlock getCustomBlock(final Block block) {
        return getCustomBlock(block.getLocation());
    }

    public List<BlockConfig> getBlockConfigs() {
        return blockConfigs;
    }
}
