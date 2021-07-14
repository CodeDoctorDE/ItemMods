package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CustomItem {
    private static final NamespacedKey dataKey = new NamespacedKey(ItemMods.getPlugin(), "data");
    private ItemStack itemStack;
    //private ItemAsset config = null;

    public CustomItem(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        //ItemMods.getMainConfig().getItems().stream().filter(itemAsset -> itemAsset.isSimilar(itemStack)).forEach(itemAsset -> config = itemAsset);
    }

    /*public ItemAsset getConfig() {
        return config;
    }*/


    public String getData() {
        return Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().getOrDefault(dataKey, PersistentDataType.STRING, "");
    }

    public void setData(String data) {
        Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, "");
    }


    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
