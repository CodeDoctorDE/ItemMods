package dev.linwood.itemmods.pack.asset;

import dev.linwood.itemmods.action.CommandAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayedAsset {
    @NotNull ItemStack getIcon(String namespace);

    default @Nullable CommandAction generateAction(String namespace) {
        return null;
    }
}
