package com.github.codedoctorde.itemmods.pack;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class BlockAsset extends PackModel {
    private HashMap<String, String> textures;
    private String model;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public HashMap<String, String> getTextures() {
        return textures;
    }
}
