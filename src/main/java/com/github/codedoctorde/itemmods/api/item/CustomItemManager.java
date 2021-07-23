package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.pack.PackObject;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItemManager {

    public CustomItemManager() {
    }

    public @Nullable ItemStack create(@NotNull PackObject packObject) {
        var asset = packObject.getItem();
        var customModel = packObject.getCustomModel();
        assert asset != null;
        var model = asset.getModel();
        assert model != null;
        assert model.getFallbackTexture() != null;
        ItemStack itemStack = new ItemStack(model.getFallbackTexture());
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(customModel);
        itemMeta.setLocalizedName(asset.getTranslatedName());
        itemMeta.setDisplayName(asset.getDisplayName());
        itemMeta.setLore(asset.getLore());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}