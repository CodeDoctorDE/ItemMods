package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author CodeDoctorDE
 */
public class PackManager {
    private static final Pattern NAME_PATTERN = Pattern.compile("/.*/");
    private final Path packDir;

    public PackManager() throws IOException {
        packDir = Paths.get(ItemMods.getPlugin().getDataFolder().getPath(), "packs");
        try {
            Files.createDirectory(packDir);
        } catch (FileAlreadyExistsException ignored) {

        }
    }

    public JsonObject createReferenceItemConfig(Path referenceItemPath) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(referenceItemPath.toString()), StandardCharsets.UTF_8));
        JsonObject jsonObject = ItemMods.getPlugin().getGson().fromJson(br, JsonObject.class);
        JsonArray array = jsonObject.getAsJsonArray("overrides");
        //ItemMods.getMainConfig().getItems().stream().filter(CustomConfig::isPack).forEach(itemConfig -> array.add(createItem(getNextIndex(array), itemConfig.getIdentifier())));
        //ItemMods.getMainConfig().getBlocks().stream().filter(CustomConfig::isPack).forEach(blockConfig -> array.add(createItem(getNextIndex(array), blockConfig.getIdentifier())));
        /*ItemMods.getApi().getAddons().forEach(addon -> {
            Arrays.stream(addon.getStaticCustomItems()).forEach(item -> array.add(createItem(getNextIndex(array), item.getIdentifier())));
            Arrays.stream(addon.getStaticCustomBlocks()).forEach(block -> array.add(createItem(getNextIndex(array), block.getIdentifier())));
        });*/

        jsonObject.add("overrides", array);
        return jsonObject;
    }

    private String[] getPacks() throws IOException {
        return Files.walk(packDir)
                .filter(Files::isDirectory).map(path -> path.getFileName().toString()).toArray(String[]::new);
    }

    private JsonObject createItem(int index, String identifier) {
        JsonObject object = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.addProperty("custom_model_data", index);
        object.add("predicate", predicate);
        object.addProperty("model", identifier);
        return object;
    }

    private int getNextIndex(JsonArray array) {
        int index = 1;
        boolean exist = false;
        while (!exist) for (JsonElement object :
                array)
            if (object.getAsJsonObject().get("predicate").isJsonObject() &&
                    object.getAsJsonObject().getAsJsonObject("predicate").get("custom_model_data").getAsInt() == index)
                exist = true;
        return index;
    }

    /**
     * @param name The name of the resource pack
     * @throws IOException              When there are problems while creating the resource pack
     * @throws IllegalArgumentException If the name is invalid
     */
    void exportDirectory(String name) throws IOException, IllegalArgumentException {
        Path outputDir = Paths.get(ItemMods.getPlugin().getDataFolder().getPath(), "export/" + name);
        Path currentDir = Paths.get(packDir.toString(), name);
        if (NAME_PATTERN.matcher(name).matches() || name.startsWith(".") || name.startsWith("/") || !Files.exists(currentDir))
            throw new IllegalArgumentException();
        Files.createDirectories(outputDir);
        for (ItemModsAddon addon :
                ItemMods.getApi().getAddons()) {
            InputStream stream = addon.getClass().getResourceAsStream("assets");
            if (stream != null)
                Files.copy(stream, outputDir);
        }
        if (!name.equals("default"))
            Files.copy(Paths.get(packDir.toString(), "default"), outputDir);
        Files.copy(currentDir, outputDir);
        Path referenceItemPath = Paths.get(outputDir.toString(), ItemMods.getMainConfig().getResourcePackConfig().getReferenceItem());
        Files.createFile(referenceItemPath);
        if (!Files.exists(referenceItemPath))
            throw new IllegalArgumentException();
        createReferenceItemConfig(referenceItemPath);
    }
}
