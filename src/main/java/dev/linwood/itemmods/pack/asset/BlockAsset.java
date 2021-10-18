package dev.linwood.itemmods.pack.asset;

import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface BlockAsset extends TemplateReadyPackAsset, CustomNamedAsset {
    @Nullable PackObject getModelObject();

    default @Nullable ModelAsset getModel() {
        var modelObject = getModelObject();
        if (modelObject == null)
            return null;
        return modelObject.getModel();
    }

    default ItemStack getModelTexture() {
        var modelObject = getModelObject();
        var model = modelObject == null ? null : modelObject.getModel();
        var itemStack = new ItemStack(model == null ? Material.STONE : model.getFallbackTexture());
        var itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(modelObject == null ? null : modelObject.getCustomModel());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Nullable PackObject getReferenceItem();
}
