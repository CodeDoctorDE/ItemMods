package com.github.codedoctorde.itemmods.pack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ItemModsPack extends NamedPackObject {
    private List<StaticItem> staticItems = new ArrayList<>();
    private List<StaticBlock> staticBlocks = new ArrayList<>();
    private List<String> dependencies = new ArrayList<>();
    private List<PackTexture> textures = new ArrayList<>();

    public List<String> getDependencies() {
        return dependencies;
    }

    public List<StaticItem> getStaticItems() {
        return staticItems;
    }
}
