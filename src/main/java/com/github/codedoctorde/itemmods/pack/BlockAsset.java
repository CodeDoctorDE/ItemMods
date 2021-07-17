package com.github.codedoctorde.itemmods.pack;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class BlockAsset extends ItemAsset {
    private HashMap<String, String> textures;

    public BlockAsset(String name) {
        super(name);
    }

    public HashMap<String, String> getTextures() {
        return textures;
    }

    @Override
    public void export(PackObject packObject, int packFormat, Path path) throws IOException {

    }
}
