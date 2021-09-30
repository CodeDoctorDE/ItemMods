package dev.linwood.itemmods.pack.custom;

import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.PackAsset;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CustomTemplate extends CustomAsset {
    public CustomTemplate(String name) {
        super(name);
    }

    public boolean isCompatible(PackObject packObject, PackAsset asset) {
        return true;
    }

    public @Nullable CommandAction generateAction(PackObject packObject, CustomData data, PackAsset asset) {
        return null;
    }

    public abstract @NotNull ItemStack getIcon(PackObject packObject, CustomData data, PackAsset asset);

    public abstract @NotNull ItemStack getMainIcon();
}
