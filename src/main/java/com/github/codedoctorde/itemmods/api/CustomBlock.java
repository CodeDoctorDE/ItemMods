package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.google.gson.JsonElement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CustomBlock {
    private Location location;
    private BlockConfig config = null;
    private ArmorStand armorStand;
    private JsonElement jsonElement;

    public CustomBlock(Location location, ArmorStand armorStand) {
        this(location);
        this.armorStand = armorStand;
    }

    public CustomBlock(Location location) {
        this.location = location;
        this.armorStand = null;
        Main.getPlugin().getMainConfig().getBlocks().stream().filter(blockConfig ->
                blockConfig.getTag().equals(getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(), "type"), PersistentDataType.STRING))).forEach(itemConfig -> config = itemConfig);
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

    public PersistentDataContainer getPersistentDataContainer() {
        if (getBlock().getState() instanceof TileState)
            return ((TileState) getBlock().getState()).getPersistentDataContainer();
        if (armorStand != null) return armorStand.getPersistentDataContainer();
        return null;
    }

    /**
     * Configure the PersistantTagContainer to the default values
     */
    public void configure() {
        System.out.println(config.getTag());
        getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "type"), PersistentDataType.STRING, config.getTag());
        getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "data"), PersistentDataType.STRING, "");
        getBlock().getState().update();
    }
}
