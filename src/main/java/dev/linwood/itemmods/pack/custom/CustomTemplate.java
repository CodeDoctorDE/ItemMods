package dev.linwood.itemmods.pack.custom;

import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.DefinedPackAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
import dev.linwood.itemmods.pack.asset.TemplateReadyPackAsset;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CustomTemplate implements PackAsset {
    private final String name;

    public @Nullable CommandAction generateItemAction(PackObject packObject, CustomData data, TemplateReadyPackAsset asset) {
        return null;
    }

    public CustomTemplate(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isCompatible(PackObject packObject, DefinedPackAsset asset) {
        return true;
    }


    public abstract @NotNull ItemStack getItemIcon(PackObject packObject, CustomData data, TemplateReadyPackAsset asset);
}
