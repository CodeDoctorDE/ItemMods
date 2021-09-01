package dev.linwood.itemmods.api.item;

import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItemManager {

    public CustomItemManager() {
    }

    public @Nullable ItemStack create(@NotNull PackObject packObject) {
        var asset = packObject.getItem();
        assert asset != null;
        assert asset.getModelObject() != null;
        var customModel = asset.getModelObject().getCustomModel();
        var model = asset.getModel();
        assert model != null;
        assert model.getFallbackTexture() != null;
        ItemStack itemStack = new ItemStack(model.getFallbackTexture());
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(customModel);
        itemMeta.setLocalizedName(asset.getLocalizedName());
        itemMeta.setDisplayName(asset.getDisplayName());
        itemMeta.setLore(asset.getLore());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}