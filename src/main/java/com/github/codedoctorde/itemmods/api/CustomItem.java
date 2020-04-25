package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.gitlab.codedoctorde.api.server.Version;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class CustomItem {
    private ItemStack itemStack;
    private ItemConfig config = null;

    public CustomItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        Main.getPlugin().getMainConfig().getItems().stream().filter(itemConfig -> itemConfig.getItemStack().isSimilar(itemStack)).forEach(itemConfig -> config = itemConfig);
    }

    public ItemConfig getConfig() {
        return config;
    }

    public String getData() {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "data");
        if (Version.getVersion().isLowerThan(Version.v1_15)) {
            String data = Objects.requireNonNull(itemStack.getItemMeta()).getCustomTagContainer().getCustomTag(key, ItemTagType.STRING);
            if (data == null)
                data = "";
            return data;
        }
        return Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }

    public void setData(String data) {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(), "data");
        if (Version.getVersion().isLowerThan(Version.v1_15))
            Objects.requireNonNull(itemStack.getItemMeta()).getCustomTagContainer().setCustomTag(key, ItemTagType.STRING, data);
        else
            Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().set(key, PersistentDataType.STRING, "");
    }


    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
