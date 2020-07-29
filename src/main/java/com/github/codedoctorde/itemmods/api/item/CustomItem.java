package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CustomItem {
    private ItemStack itemStack;
    private ItemConfig config = null;
    private static final NamespacedKey dataKey = new NamespacedKey(ItemMods.getPlugin(), "data");

    public CustomItem(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        ItemMods.getPlugin().getMainConfig().getItems().stream().filter(itemConfig -> itemConfig.isSimilar(itemStack)).forEach(itemConfig -> config = itemConfig);
    }

    public ItemConfig getConfig() {
        return config;
    }


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
