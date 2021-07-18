package com.github.codedoctorde.itemmods.pack.asset;

import com.github.codedoctorde.itemmods.pack.PackObject;

import java.util.HashMap;

public class BlockAsset extends ItemAsset {
    private HashMap<String, String> textures;
    private PackObject blockModel;

    public BlockAsset(String name) {
        super(name);
    }

    public HashMap<String, String> getTextures() {
        return textures;
    }

    public PackObject getBlockModel() {
        return blockModel;
    }

    public void setBlockModel(PackObject blockModel) {
        assert blockModel.getModel() != null;
        this.blockModel = blockModel;
    }
}
