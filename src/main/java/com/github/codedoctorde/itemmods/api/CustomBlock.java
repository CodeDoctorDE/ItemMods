package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.google.gson.JsonElement;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class CustomBlock {
    private final Location location;
    private final BlockConfig config;
    private final ArmorStand armorStand;
    private JsonElement jsonElement;
    private BlockConfig blockConfig;

    public CustomBlock(BlockConfig config, Location location, ArmorStand armorStand) {
        this.location = location;
        this.config = config;
        this.armorStand = armorStand;
    }

    public CustomBlock(BlockConfig config, Location location) {
        this.location = location;
        this.config = config;
        this.armorStand = null;
    }

    public BlockConfig getConfig() {
        return config;
    }

    public Location getLocation() {
        return location;
    }

    @Nullable
    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void breakBlock() {
        location.getBlock().breakNaturally();
    }

    public Block getBlock() {
        return location.getBlock();
    }

    public PersistentDataContainer getPersistentDataContainer() {
        if (getBlock().getState() instanceof TileState)
            return ((TileState) getBlock().getState()).getPersistentDataContainer();
        else if (armorStand != null) return armorStand.getPersistentDataContainer();
        return null;
    }

    /**
     * Configure the PersistantTagContainer to the default values
     */
    public void configure() {
        getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "type"), PersistentDataType.STRING, blockConfig.getTag());
        getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "data"), PersistentDataType.STRING, "");
    }
}
