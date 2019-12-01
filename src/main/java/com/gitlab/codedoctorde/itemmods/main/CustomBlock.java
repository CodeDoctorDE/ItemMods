package com.gitlab.codedoctorde.itemmods.main;

import com.gitlab.codedoctorde.itemmods.config.BlockConfig;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

public class CustomBlock {
    private final Location location;
    private final BlockConfig config;
    private final ArmorStand armorStand;

    public CustomBlock(BlockConfig config, Location location, ArmorStand armorStand) {
        this.location = location;
        this.config = config;
        this.armorStand = armorStand;
    }

    public BlockConfig getConfig() {
        return config;
    }

    public Location getLocation() {
        return location;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void breakBlock() {
        location.getBlock().breakNaturally();
    }

    public Block getBlock() {
        return location.getBlock();
    }
}
