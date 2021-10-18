package dev.linwood.itemmods.pack;

import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.pack.asset.TemplateReadyPackAsset;
import dev.linwood.itemmods.pack.custom.CustomData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayedObject {
    default @Nullable CommandAction generateAction(PackObject packObject, CustomData data, TemplateReadyPackAsset asset) {
        return null;
    }

    @NotNull ItemStack getIcon();
}
