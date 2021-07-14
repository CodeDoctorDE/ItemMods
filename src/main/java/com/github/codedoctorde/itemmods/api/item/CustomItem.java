package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.ItemAsset;
import com.github.codedoctorde.itemmods.pack.PackObject;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CustomItem {
    private static final NamespacedKey TYPE_KEY = new NamespacedKey(ItemMods.getPlugin(), "type");
    private static final NamespacedKey DATA_KEY = new NamespacedKey(ItemMods.getPlugin(), "data");
    private final ItemStack itemStack;
    private final ItemAsset config = null;

    public CustomItem(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemAsset getConfig() {
        var packObject = PackObject.fromIdentifier(Objects.requireNonNull(Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().get(TYPE_KEY, PersistentDataType.STRING)));
        if (packObject == null)
            return null;
        return packObject.getItem();
    }


    public String getData() {
        return Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().getOrDefault(DATA_KEY, PersistentDataType.STRING, "");
    }

    public void setData(String data) {
        Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().set(DATA_KEY, PersistentDataType.STRING, "");
    }

    private String getType() {
        return Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().get(TYPE_KEY, PersistentDataType.STRING);
    }


    public ItemStack getItemStack() {
        return itemStack;
    }
}
