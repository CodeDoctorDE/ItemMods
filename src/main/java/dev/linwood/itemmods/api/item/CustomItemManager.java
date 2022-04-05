package dev.linwood.itemmods.api.item;

import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItemManager {
    private static CustomItemManager instance;

    private CustomItemManager() {
    }

    public static CustomItemManager getInstance() {
        return instance == null ? instance = new CustomItemManager() : instance;
    }

    public ItemAsset getAssetByKey(String key) throws UnsupportedOperationException {
        var packObject = new PackObject(key);
        return packObject.getAsset(ItemAsset.class);
    }

    public CustomItem fromItemStack(ItemStack itemStack) {
        return new CustomItem(itemStack);
    }

    public @Nullable
    CustomItem create(@NotNull PackObject packObject) {
        var asset = packObject.getAsset(ItemAsset.class);
        if (asset == null || asset.getModelObject() == null)
            return null;
        var customModel = asset.getModelObject().getCustomModel();
        var model = asset.getModel();
        assert model != null;
        ItemStack itemStack = new ItemStack(model.getFallbackTexture());
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(customModel);
        var displayName = asset.getDisplayName();
        if (displayName != null)
            if (displayName.isTranslated())
                itemMeta.setLocalizedName(displayName.getName());
            else
                itemMeta.setDisplayName(displayName.getName());
        itemMeta.setLore(asset.getLore());
        itemMeta.getPersistentDataContainer().set(CustomItem.TYPE_KEY, PersistentDataType.STRING, packObject.toString());
        itemStack.setItemMeta(itemMeta);

        return new CustomItem(itemStack);
    }
}