package com.github.codedoctorde.itemmods.pack.custom;

import com.github.codedoctorde.itemmods.pack.PackObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CustomTemplate {
    public abstract @NotNull String getName();

    public abstract @NotNull ItemStack getIcon(CustomTemplateData data);

    public abstract @NotNull ItemStack getMainIcon();

    public boolean isCompatible(PackObject object) {
        return true;
    }

    public boolean openConfigGui(CustomTemplateData data, Player player) {
        return false;
    }
}
