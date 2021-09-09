package dev.linwood.itemmods.api.item;

import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItemManager {

    public CustomItemManager() {
    }

    public ItemAsset getAssetByKey(String key) {
        var packObject = new PackObject(key);
        if(packObject != null)
            return packObject.getItem();
        return null;
    }

    public CustomItem fromItemStack(ItemStack itemStack) {
        return new CustomItem(itemStack);
    }

    public @Nullable CustomItem create(@NotNull PackObject packObject) {
        var asset = packObject.getItem();
        if (asset == null || asset.getModelObject() == null)
            return null;
        var customModel = asset.getModelObject().getCustomModel();
        var model = asset.getModel();
        assert model != null;
        ItemStack itemStack = new ItemStack(model.getFallbackTexture());
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(customModel);
        itemMeta.setLocalizedName(asset.getLocalizedName());
        itemMeta.setDisplayName(asset.getDisplayName());
        itemMeta.setLore(asset.getLore());
        itemMeta.getPersistentDataContainer().set(CustomItem.TYPE_KEY, PersistentDataType.STRING, packObject.toString());
        itemStack.setItemMeta(itemMeta);
        return new CustomItem(itemStack);
    }
}