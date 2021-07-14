package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.pack.PackObject;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class CustomItemManager {

    public CustomItemManager() {
    }

    public ItemStack create(PackObject packObject) {
        return Objects.requireNonNull(packObject.getItem()).create();
    }
}