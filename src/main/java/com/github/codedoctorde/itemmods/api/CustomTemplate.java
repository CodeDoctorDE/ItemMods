package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.config.CustomConfig;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CustomTemplate<C extends CustomConfig, T> {
    private final String name;

    public CustomTemplate(String name){
        this.name = name;
    }
    public abstract void onLoad(T target);
    public abstract void onUnload(T target);

    @NotNull
    public abstract ItemStack getIcon(C config);

    @NotNull
    public abstract ItemStack getMainIcon(C config);

    public abstract boolean isCompatible(C config);

    public abstract boolean openConfigGui(C config, Player player);

    public String getName() {
        return name;
    }
}
