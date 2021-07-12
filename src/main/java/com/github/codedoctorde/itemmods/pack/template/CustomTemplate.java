package com.github.codedoctorde.itemmods.pack.template;

import com.github.codedoctorde.itemmods.pack.NamedPackObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CustomTemplate<C extends NamedPackObject, T> {
    String getName();

    void onLoad(T target);

    void onUnload(T target);

    @NotNull ItemStack createIcon(C config);

    @NotNull ItemStack createMainIcon(C config);

    boolean isCompatible(C config);

    boolean openConfigGui(C config, Player player);
}
