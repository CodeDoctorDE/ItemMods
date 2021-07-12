package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.itemmods.ItemMods;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PackItem extends TexturedPackObject {
    private ItemModsPack pack;

    public PackItem() {

    }

    @Override
    void export(ItemModsPack pack, Path path) throws IOException {
        var filePath = Paths.get(path.toString(), getName());
        JsonObject jsonObject = ItemMods.getPlugin().getGson().fromJson(Files.newBufferedReader(filePath), JsonObject.class);
        JsonArray array = jsonObject.getAsJsonArray("overrides");
        /* ItemMods.getMainConfig().getItems().stream().filter(CustomConfig::isPack).forEach(itemConfig -> array.add(createItem(getNextIndex(array), itemConfig.getIdentifier())));
        ItemMods.getMainConfig().getBlocks().stream().filter(CustomConfig::isPack).forEach(blockConfig -> array.add(createItem(getNextIndex(array), blockConfig.getIdentifier())));
        ItemMods.getApi().getAddons().forEach(addon -> {
            Arrays.stream(addon.getStaticCustomItems()).forEach(item -> array.add(createItem(getNextIndex(array), item.getIdentifier())));
            Arrays.stream(addon.getStaticCustomBlocks()).forEach(block -> array.add(createItem(getNextIndex(array), block.getIdentifier())));
        });*/

        jsonObject.add("overrides", array);
        Files.writeString(path, GSON.toJson(jsonObject));
    }

    @Override
    void save(ItemModsPack pack, Path path) throws IOException {

    }

    @Override
    void load(ItemModsPack pack, Path path) throws IOException {

    }
}
