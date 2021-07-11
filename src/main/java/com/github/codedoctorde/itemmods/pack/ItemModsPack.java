package com.github.codedoctorde.itemmods.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ItemModsPack extends NamedPackObject {
    private final List<StaticItem> staticItems = new ArrayList<>();
    private final List<StaticBlock> staticBlocks = new ArrayList<>();
    private final List<String> dependencies = new ArrayList<>();
    private final List<PackTexture> textures = new ArrayList<>();

    public List<String> getDependencies() {
        return dependencies;
    }

    public List<StaticItem> getStaticItems() {
        return staticItems;
    }

    void save(ItemModsPack pack, Path path) {

    }

    @Override
    void load(ItemModsPack pack, Path path) {

    }

    @Override
    void export(ItemModsPack pack, Path path) {

    }
}
