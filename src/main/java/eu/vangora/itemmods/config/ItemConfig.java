package eu.vangora.itemmods.config;

import eu.vangora.itemmods.main.ArmorType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemConfig {
    private String name;
    private String displayName;
    private ItemStack itemStack;
    private boolean canRename = true;
    private int pickaxe = 0;
    private int shovel = 0;
    private int axe = 0;
    private int hoe = 0;
    private int damage = 0;
    private int speed = 0;
    private boolean boneMeal = false;
    private ArmorType armorType = ArmorType.NONE;
    private List<String> onWear = new ArrayList<>();
    private List<String> onOffHand = new ArrayList<>();
    private List<String> onMainHand = new ArrayList<>();
    private List<String> onDrop = new ArrayList<>();
    private List<String> onPickup = new ArrayList<>();
    private List<String> onRightClick = new ArrayList<>();


    public ItemConfig(String name) {
        this.name = name;
        this.displayName = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean isCanRename() {
        return canRename;
    }

    public void setCanRename(boolean canRename) {
        this.canRename = canRename;
    }

    public int getAxe() {
        return axe;
    }

    public void setAxe(int axe) {
        this.axe = axe;
    }

    public int getHoe() {
        return hoe;
    }

    public void setHoe(int hoe) {
        this.hoe = hoe;
    }

    public int getPickaxe() {
        return pickaxe;
    }

    public void setPickaxe(int pickaxe) {
        this.pickaxe = pickaxe;
    }

    public int getShovel() {
        return shovel;
    }

    public void setShovel(int shovel) {
        this.shovel = shovel;
    }

    public boolean isBoneMeal() {
        return boneMeal;
    }

    public void setBoneMeal(boolean boneMeal) {
        this.boneMeal = boneMeal;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ArmorType getArmorType() {
        return armorType;
    }

    public void setArmorType(ArmorType armorType) {
        this.armorType = armorType;
    }

    public List<String> getOnDrop() {
        return onDrop;
    }

    public List<String> getOnWear() {
        return onWear;
    }

    public List<String> getOnMainHand() {
        return onMainHand;
    }

    public List<String> getOnOffHand() {
        return onOffHand;
    }

    public List<String> getOnPickup() {
        return onPickup;
    }

    public List<String> getOnRightClick() {
        return onRightClick;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
