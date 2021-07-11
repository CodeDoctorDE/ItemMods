package com.github.codedoctorde.itemmods.pack;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class StaticBlock extends NamedPackObject {
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

    @Override
    void export(ItemModsPack pack, Path path) throws IOException {

    }

    @Override
    void save(ItemModsPack pack, Path path) throws IOException {

    }

    @Override
    void load(ItemModsPack pack, Path path) throws IOException {

    }
}
