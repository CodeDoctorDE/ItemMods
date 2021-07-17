package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomModel {
    private PackObject packObject = null;
    private Material fallbackTexture = Material.STONE;
    private Integer staticModel = null;

    public CustomModel() {
    }

    public boolean isStatic() {
        return staticModel != null;
    }

    public boolean isCustom() {
        return packObject == null;
    }

    public Integer getStaticModel() {
        return staticModel;
    }

    public void setStaticModel(Integer staticModel) {
        this.staticModel = staticModel;
    }

    public Material getFallbackTexture() {
        return fallbackTexture;
    }

    public void setFallbackTexture(Material fallbackTexture) {
        this.fallbackTexture = fallbackTexture;
    }

    public Integer getModel() {
        if (staticModel != null)
            return staticModel;
        return ItemMods.getMainConfig().getResourcePackConfig().getResourceIdentifier().get(packObject.toString());
    }

    public PackObject getPackObject() {
        return packObject;
    }

    public void setPackObject(PackObject packObject) {
        this.packObject = packObject;
    }

    public ItemStack create() {
        ItemStack itemStack = new ItemStack(fallbackTexture);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(getModel());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
