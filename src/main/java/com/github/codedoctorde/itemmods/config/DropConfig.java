package com.github.codedoctorde.itemmods.config;

import org.bukkit.inventory.ItemStack;

/**
 * @author CodeDoctorDE
 */
public class DropConfig {
    private int rarity = 100;
    private ItemStack itemStack;
    private boolean fortune = false;

    public DropConfig(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean isFortune() {
        return fortune;
    }

    public void setFortune(boolean fortune) {
        this.fortune = fortune;
    }
}
