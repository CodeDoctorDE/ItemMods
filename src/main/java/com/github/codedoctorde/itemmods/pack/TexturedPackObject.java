package com.github.codedoctorde.itemmods.pack;

import java.util.Map;

public abstract class TexturedPackObject extends NamedPackObject {
    private String model;
    private Map<String, String> textures;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (!NAME_PATTERN.matcher(model).matches())
            throw new UnsupportedOperationException();
        this.model = model;
    }

    public Map<String, String> getTextures() {
        return textures;
    }
}
