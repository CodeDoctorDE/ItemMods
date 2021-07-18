package com.github.codedoctorde.itemmods.pack.asset;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.raw.ModelAsset;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class BlockAsset extends ItemAsset {
    private HashMap<String, String> textures;
    private PackObject blockModelObject;

    public BlockAsset(String name) {
        super(name);
    }

    public HashMap<String, String> getTextures() {
        return textures;
    }

    public @Nullable PackObject getBlockModelObject() {
        return blockModelObject;
    }

    public void setBlockModelObject(@Nullable PackObject modelObject) {
        if (modelObject == null)
            this.blockModelObject = null;
        else if (modelObject.getModel() != null)
            this.blockModelObject = modelObject;
    }

    @Nullable
    public ModelAsset getBlockModel() {
        if (blockModelObject == null)
            return null;
        return blockModelObject.getModel();
    }


    @NotNull
    @Override
    public Material getIcon() {
        var model = getModel();
        if (model == null || model.getFallbackTexture() == null)
            return Material.GRASS_BLOCK;
        return model.getFallbackTexture();
    }
}
