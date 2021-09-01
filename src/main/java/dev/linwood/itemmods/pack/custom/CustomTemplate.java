package dev.linwood.itemmods.pack.custom;

import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CustomTemplate {
    public abstract @NotNull String getName();

    public abstract @NotNull ItemStack getIcon(PackObject packObject, CustomTemplateData data);

    public abstract @NotNull ItemStack getMainIcon();

    public boolean isCompatible(PackObject packObject) {
        return true;
    }

    public boolean openConfigGui(PackObject packObject, CustomTemplateData data, Player player) {
        return false;
    }
}
