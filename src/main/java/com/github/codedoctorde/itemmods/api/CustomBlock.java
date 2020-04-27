package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.google.gson.JsonElement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CustomBlock {
    private Location location;
    private BlockConfig config = null;
    private ArmorStand armorStand;
    private JsonElement jsonElement;

    public CustomBlock(Location location, BlockConfig config) {
        this(location, null, config);
    }

    public CustomBlock(Location location, ArmorStand armorStand, BlockConfig config) {
        this.config = config;
        this.location = location;
        this.armorStand = armorStand;
        if (getType() == null)
            configure();
    }

    public CustomBlock(Location location, ArmorStand armorStand) {
        this(location);
        this.armorStand = armorStand;
    }

    public CustomBlock(Location location) {
        this.location = location;
        this.armorStand = null;
        Main.getPlugin().getMainConfig().getBlocks().stream().filter(blockConfig ->
                blockConfig.getTag().equals(getType())).forEach(itemConfig -> config = itemConfig);
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

    public String getType() {
        return getString(new NamespacedKey(Main.getPlugin(), "type"));
    }

    public void setType(String type) {
        setString(new NamespacedKey(Main.getPlugin(), "type"), type);
    }

    public void breakBlock(boolean drops) {
        getBlock().setType(Material.AIR);
        getBlock().getDrops().clear();
        if (!getConfig().isDrop() || getConfig().isDrop() && drops)
            getConfig().getDrops().stream().filter(drop -> new Random().nextInt(99) + 1 <= drop.getRarity()).forEach(drop -> getBlock().getWorld().dropItem(getBlock().getLocation(), drop.getItemStack()));
        if (getArmorStand() != null)
            getArmorStand().remove();
    }

    public Block getBlock() {
        return location.getBlock();
    }

    public String getString(NamespacedKey key) {
        BlockState blockState = getBlock().getState();
        if (blockState instanceof TileState) {
            TileState tileState = (TileState) blockState;
            return tileState.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }
        if (armorStand != null) return armorStand.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return null;
    }

    public void setString(NamespacedKey key, String value) {
        BlockState blockState = getBlock().getState();
        if (blockState instanceof TileState) {
            TileState tileState = (TileState) blockState;
            tileState.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
            tileState.update();
        }
        if (armorStand != null) {
            armorStand.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        }
    }

    /**
     * Configure the PersistantTagContainer to the default values
     */
    public void configure() {
        System.out.println(config.getTag());
        setString(new NamespacedKey(Main.getPlugin(), "type"), config.getTag());
        setString(new NamespacedKey(Main.getPlugin(), "data"), "");
    }
}
