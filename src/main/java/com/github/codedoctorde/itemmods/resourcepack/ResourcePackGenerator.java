package com.github.codedoctorde.itemmods.resourcepack;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.MainConfig;
import com.github.codedoctorde.itemmods.config.ResourcePackConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author CodeDoctorDE
 */
public class ResourcePackGenerator {
    private final File packDir;
    private final JsonObject translation;
    private final File configFile = new File(ItemMods.getPlugin().getDataFolder(), "config.json");
    public ResourcePackGenerator() {
        packDir = new File(ItemMods.getPlugin().getDataFolder().getAbsolutePath(), "pack");
        translation = ItemMods.getPlugin().getTranslationConfig().getJsonObject("pack");
        if(!packDir.mkdirs())
            Bukkit.getConsoleSender().sendMessage(translation.get("created").getAsString());
    }
    public JsonObject createReferenceItemConfig(File referenceItemFile) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(referenceItemFile), StandardCharsets.UTF_8));
        JsonObject jsonObject = ItemMods.getPlugin().getGson().fromJson(br, JsonObject.class);
        JsonArray array = jsonObject.getAsJsonArray("overrides");
        return jsonObject;
    }
    public boolean exportDirectory() throws IOException {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        File outputDir = new File(ItemMods.getPlugin().getDataFolder(), "pack");
        if(outputDir.exists()) if (outputDir.delete())
            sender.sendMessage(translation.get("delete").getAsString());
        if(outputDir.mkdirs())
            sender.sendMessage(translation.get("create").getAsString());
        FileUtils.copyDirectory(packDir, outputDir);
        File referenceItemFile = new File(outputDir, "assets/minecraft/models/" + ItemMods.getPlugin().getMainConfig().getResourcePackConfig().getReferenceItem() + ".json");
        if (!referenceItemFile.exists() && (referenceItemFile.getParentFile().mkdirs() || !referenceItemFile.createNewFile()))
            return false;


        return true;
    }
}
