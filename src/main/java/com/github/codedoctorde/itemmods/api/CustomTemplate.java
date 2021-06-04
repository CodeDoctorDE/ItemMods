package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.config.CustomConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CustomTemplate<C extends CustomConfig, T> {
    String getName();

    void onLoad(T target);

    void onUnload(T target);

    @NotNull ItemStack createIcon(C config);

    @NotNull ItemStack createMainIcon(C config);

    boolean isCompatible(C config);

    boolean openConfigGui(C config, Player player);
}
