package com.github.codedoctorde.itemmods.config;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * @author CodeDoctorDE
 */
public class ArmorStandBlockConfig {
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack mainHand;
    private ItemStack offHand;
    private boolean small = false;
    private boolean basePlate = true;
    private boolean invisible = true;
    private boolean marker = false;
    private boolean invulnerable = true;
    private boolean customNameVisible = false;
    private final boolean showingArms = true;
    private String customName;
    private int gravity = 5;
    private int stable = 5;

    public boolean isBasePlate() {
        return basePlate;
    }

    public void setBasePlate(boolean basePlate) {
        this.basePlate = basePlate;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isSmall() {
        return small;
    }

    public void setSmall(boolean small) {
        this.small = small;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public void setMainHand(ItemStack mainHand) {
        this.mainHand = mainHand;
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    public void setOffHand(ItemStack offHand) {
        this.offHand = offHand;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public boolean isCustomNameVisible() {
        return customNameVisible;
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        this.customNameVisible = customNameVisible;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public boolean isMarker() {
        return marker;
    }

    public void setMarker(boolean marker) {
        this.marker = marker;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getStable() {
        return stable;
    }

    public void setStable(int stable) {
        this.stable = stable;
    }

    public ArmorStand spawn(Location location) {
        ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(location.getWorld()).spawnEntity(location.clone().add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
        if (armorStand.getEquipment() == null)
            return null;
        armorStand.setVisible(!isInvisible());
        armorStand.setSmall(isSmall());
        armorStand.setMarker(isMarker());
        armorStand.setInvulnerable(isInvulnerable());
        armorStand.setCustomNameVisible(isCustomNameVisible());
        armorStand.setCustomName(getCustomName());
        armorStand.setGravity(false);
        armorStand.setSilent(true);
        armorStand.setCollidable(false);
        armorStand.setBasePlate(isBasePlate());

        EntityEquipment equipment = armorStand.getEquipment();
        equipment.setHelmet(getHelmet());
        equipment.setChestplate(getChestplate());
        equipment.setLeggings(getLeggings());
        equipment.setBoots(getBoots());
        equipment.setItemInMainHand(getMainHand());
        equipment.setItemInOffHand(getOffHand());
        return armorStand;
    }
}
