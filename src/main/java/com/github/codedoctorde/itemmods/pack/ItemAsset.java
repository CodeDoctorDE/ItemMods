package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemAsset extends PackAsset {
    private String translatedDisplayName = null;
    private String displayName = null;
    private List<String> lore = new ArrayList<>();
    private CustomModel model;

    public String getTranslatedDisplayName() {
        return translatedDisplayName;
    }

    public void setTranslatedDisplayName(String translatedDisplayName) {
        this.translatedDisplayName = translatedDisplayName;
    }

    public void removeTranslatedDisplayName() {
        translatedDisplayName = null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void removeDisplayName() {
        displayName = null;
    }

    public List<String> getLore() {
        return Collections.unmodifiableList(lore);
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public CustomModel getModel() {
        return model;
    }

    public Translation getTranslation() {
        return ItemMods.getTranslationConfig().subTranslation("addon,pack.item");
    }

    @Override
    public void export(PackObject packObject, int packFormat, Path path) throws IOException {
        var filePath = Paths.get(path.toString(), packObject.getName());
        var jsonObject = ItemMods.getPlugin().getGson().fromJson(Files.newBufferedReader(filePath), JsonObject.class);
        var array = jsonObject.getAsJsonArray("overrides");
        /* ItemMods.getMainConfig().getItems().stream().filter(CustomConfig::isPack).forEach(itemAsset -> array.add(createItem(getNextIndex(array), itemAsset.getIdentifier())));
        ItemMods.getMainConfig().getBlocks().stream().filter(CustomConfig::isPack).forEach(blockConfig -> array.add(createItem(getNextIndex(array), blockConfig.getIdentifier())));
        ItemMods.getApi().getAddons().forEach(addon -> {
            Arrays.stream(addon.getStaticCustomItems()).forEach(item -> array.add(createItem(getNextIndex(array), item.getIdentifier())));
            Arrays.stream(addon.getStaticCustomBlocks()).forEach(block -> array.add(createItem(getNextIndex(array), block.getIdentifier())));
        });*/

        jsonObject.add("overrides", array);
        Files.writeString(path, GSON.toJson(jsonObject));
    }

    public ItemStack create() {
        return new ItemStackBuilder(getModel().create()).build();
    }
}
