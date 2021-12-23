package dev.linwood.itemmods.pack.custom;

import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.CustomPackAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CustomTemplate extends PackAsset {
    public CustomTemplate(String name) {
        super(name);
    }

    public @Nullable CommandAction generateItemAction(PackObject packObject, CustomData data, CustomPackAsset asset) {
        return null;
    }

    public boolean isCompatible(PackObject packObject, CustomPackAsset asset) {
        return true;
    }


    public abstract @NotNull ItemStack getPreviewIcon();

    public abstract @NotNull ItemStack getItemIcon(PackObject packObject, CustomData data, CustomPackAsset asset);
}
