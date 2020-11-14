package com.github.codedoctorde.itemmods.resourcepack;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.config.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CodeDoctorDE
 */
public class PackManager {
    private final File packDir;
    private final JsonObject translation;
    private final File configFile = new File(ItemMods.getPlugin().getDataFolder(), "config.json");
    public PackManager() {
        packDir = new File(ItemMods.getPlugin().getDataFolder().getAbsolutePath(), "pack");
        translation = ItemMods.getPlugin().getTranslationConfig().getJsonObject("pack");
        if(!packDir.mkdirs())
            Bukkit.getConsoleSender().sendMessage(translation.get("created").getAsString());
    }
    public JsonObject createReferenceItemConfig(File referenceItemFile) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(referenceItemFile), StandardCharsets.UTF_8));
        JsonObject jsonObject = ItemMods.getPlugin().getGson().fromJson(br, JsonObject.class);
        JsonArray array = jsonObject.getAsJsonArray("overrides");
        ItemMods.getPlugin().getMainConfig().getItems().stream().filter(CustomConfig::isPack).forEach(itemConfig -> array.add(createItem(getNextIndex(array), itemConfig.getIdentifier())));
        ItemMods.getPlugin().getMainConfig().getBlocks().stream().filter(CustomConfig::isPack).forEach(blockConfig -> array.add(createItem(getNextIndex(array), blockConfig.getIdentifier())));
        ItemMods.getPlugin().getApi().getAddons().forEach(addon -> {
            Arrays.stream(addon.getStaticCustomItems()).forEach(item -> array.add(createItem(getNextIndex(array), item.getIdentifier())));
            Arrays.stream(addon.getStaticCustomBlocks()).forEach(block -> array.add(createItem(getNextIndex(array), block.getIdentifier())));
        });

        jsonObject.add("overrides", array);
        return jsonObject;
    }
    private JsonObject createItem(int index, String tag){
        JsonObject object = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.addProperty("custom_model_data", index);
        object.add("predicate", predicate);
        object.addProperty("model", tag);
        return object;
    }
    private int getNextIndex(JsonArray array){
        int index = 1;
        boolean exist = false;
        while(!exist) for (JsonElement object :
                array)
            if (object.getAsJsonObject().get("predicate").isJsonObject() &&
                    object.getAsJsonObject().getAsJsonObject("predicate").get("custom_model_data").getAsInt() == index)
                exist = true;
        return index;
    }
    public boolean exportDirectory(String name) throws IOException {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        File outputDir = new File(ItemMods.getPlugin().getDataFolder(), "pack");
        if(outputDir.exists()) if (outputDir.delete())
            sender.sendMessage(translation.get("delete").getAsString());
        if(outputDir.mkdirs())
            sender.sendMessage(translation.get("create").getAsString());
        for (ItemModsAddon addon:
             ItemMods.getPlugin().getApi().getAddons()) {
            URL url = addon.getClass().getResource("assets");
            if(url != null)
                FileUtils.copyURLToFile(url, packDir);
        }
        FileUtils.copyDirectory(packDir, outputDir);
        File referenceItemFile = new File(outputDir, ItemMods.getPlugin().getMainConfig().getResourcePackConfig().getReferenceItem());
        if (!referenceItemFile.exists() && (referenceItemFile.getParentFile().mkdirs() || !referenceItemFile.createNewFile()))
            return false;
        createReferenceItemConfig(referenceItemFile);


        return true;
    }
}
