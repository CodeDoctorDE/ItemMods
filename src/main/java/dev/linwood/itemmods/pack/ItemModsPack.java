package dev.linwood.itemmods.pack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.linwood.api.utils.FileUtils;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import dev.linwood.itemmods.pack.asset.CustomPackAsset;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import dev.linwood.itemmods.pack.asset.raw.TextureAsset;
import dev.linwood.itemmods.pack.custom.CustomGenerator;
import dev.linwood.itemmods.pack.custom.CustomTemplate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static dev.linwood.itemmods.ItemMods.GSON;

public class ItemModsPack implements NamedPackObject {
    public static final Pattern NAME_PATTERN = Pattern.compile("^[a-z_\\-]+$");
    private final boolean editable;
    private final List<String> dependencies = new ArrayList<>();
    private final List<PackAsset> assets = new ArrayList<>();
    private String name;
    private @NotNull Material icon = Material.GRASS_BLOCK;
    private String description = "";

    public ItemModsPack(@NotNull String name, boolean editable) throws UnsupportedOperationException {
        this.name = name;
        this.editable = editable;
    }

    public ItemModsPack(@NotNull String name) {
        this(name, true);
    }

    public ItemModsPack(@NotNull Path path) throws IOException {
        this(path.getFileName().toString(), true);
        var br = Files.newBufferedReader(Paths.get(path.toString(), "pack.json"));
        JsonObject jsonObject = GSON.fromJson(br, JsonObject.class);
        br.close();
        if (jsonObject.has("icon") && jsonObject.get("icon").isJsonPrimitive())
            icon = Objects.requireNonNull(Material.getMaterial(jsonObject.get("icon").getAsString()));
        jsonObject.getAsJsonArray("dependencies").forEach(jsonElement -> dependencies.add(jsonElement.getAsString()));
        var itemsPath = Paths.get(path.toString(), "items");
        Files.walk(itemsPath).filter(Files::isRegularFile).forEach(current -> {
            try {
                registerFile(current, ItemAsset.class);
            } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        var blocksPath = Paths.get(path.toString(), "blocks");
        Files.walk(blocksPath).filter(Files::isRegularFile).forEach(current -> {
            try {
                registerFile(current, BlockAsset.class);
            } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        var modelsPath = Paths.get(path.toString(), "models");
        Files.walk(modelsPath).filter(Files::isRegularFile).forEach(current -> {
            try {
                registerFile(current, ModelAsset.class);
            } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        var texturesPath = Paths.get(path.toString(), "textures");
        Files.walk(texturesPath).filter(Files::isRegularFile).forEach(current -> {
            try {
                registerFile(current, TextureAsset.class);
            } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private <T extends PackAsset> void registerFile(@NotNull Path path, @NotNull Class<T> type) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var fileName = FileUtils.getFileName(path);
        var asset = type.getConstructor(String.class, JsonObject.class).newInstance(fileName, GSON.fromJson(Files.readString(path), JsonObject.class));
        register(asset);
    }

    public <T extends PackAsset> void register(T asset) {
        if (PackObject.NAME_PATTERN.matcher(asset.getName()).matches())
            assets.add(asset);
    }

    public void unregister(String name) {
        assets.removeIf(asset -> asset.getName().equals(name));
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) throws UnsupportedOperationException {
        if (!NAME_PATTERN.matcher(name).matches())
            throw new UnsupportedOperationException();
        this.name = name;
    }

    public @NotNull List<String> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    public void registerDependency(@NotNull String name) {
        if (PackObject.NAME_PATTERN.matcher(name).matches())
            dependencies.add(name);
    }

    public void unregisterDependency(String name) {
        dependencies.removeIf(dependency -> dependency.equals(name));
    }

    public List<PackAsset> getAssets() {
        return Collections.unmodifiableList(assets);
    }

    public <T extends PackAsset> List<? extends T> getAssets(Class<T> type) {
        return assets.stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
    }

    public @NotNull List<? extends CustomTemplate> getTemplates() {
        return getAssets(CustomTemplate.class);
    }

    public @NotNull <T extends CustomPackAsset> List<CustomGenerator<T>> getGenerators(@NotNull Class<T> type) {
        //noinspection unchecked
        return (List<CustomGenerator<T>>) getAssets(CustomGenerator.class);
    }

    public @NotNull List<CustomGenerator<?>> getGenerators() {
        //noinspection unchecked
        return (List<CustomGenerator<?>>) getAssets(CustomGenerator.class);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEditable() {
        return editable;
    }

    public @NotNull ItemStack getIcon() {
        return new ItemStack(icon);
    }

    public void setIcon(@NotNull Material icon) {
        this.icon = icon;
    }

    void save(@NotNull Path path) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("icon", new JsonPrimitive(icon.name()));
        var dependenciesArray = new JsonArray();
        dependencies.forEach(dependenciesArray::add);
        jsonObject.add("dependencies", dependenciesArray);
        jsonObject.addProperty("version", ItemMods.FILE_VERSION);
        Files.writeString(Paths.get(path.toString(), "pack.json"), GSON.toJson(jsonObject));

        var itemsDir = Paths.get(path.toString(), "items");
        if (!Files.exists(itemsDir))
            Files.createDirectories(itemsDir);
        getAssets(ItemAsset.class).forEach(itemAsset -> {
            var current = itemAsset.save(getName());
            try {
                var currentPath = Paths.get(itemsDir.toString(), itemAsset.getName() + ".json");
                Files.createDirectories(currentPath.getParent());
                Files.writeString(currentPath, GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var blocksDir = Paths.get(path.toString(), "blocks");
        if (!Files.exists(blocksDir))
            Files.createDirectories(blocksDir);
        getAssets(BlockAsset.class).forEach(blockAsset -> {
            var current = blockAsset.save(getName());
            try {
                var currentPath = Paths.get(blocksDir.toString(), blockAsset.getName() + ".json");
                Files.createDirectories(currentPath.getParent());
                Files.writeString(currentPath, GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var texturesDir = Paths.get(path.toString(), "textures");
        if (!Files.exists(texturesDir))
            Files.createDirectories(texturesDir);
        getAssets(TextureAsset.class).forEach(textureAsset -> {
            var current = textureAsset.save(getName());
            try {
                var currentPath = Paths.get(texturesDir.toString(), textureAsset.getName() + ".json");
                Files.createDirectories(currentPath.getParent());
                Files.writeString(Paths.get(texturesDir.toString(), textureAsset.getName() + ".json"), GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var modelsDir = Paths.get(path.toString(), "models");
        if (!Files.exists(modelsDir))
            Files.createDirectories(modelsDir);
        getAssets(ModelAsset.class).forEach(modelAsset -> {
            var current = modelAsset.save(getName());
            try {
                var currentPath = Paths.get(modelsDir.toString(), modelAsset.getName() + ".json");
                Files.createDirectories(currentPath.getParent());
                Files.writeString(currentPath, GSON.toJson(current));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void export(String variation, int packFormat, @NotNull Path path) throws IOException {
        for (ModelAsset model : getAssets(ModelAsset.class)) model.export(getName(), variation, packFormat, path);
        for (TextureAsset texture : getAssets(TextureAsset.class))
            texture.export(getName(), variation, packFormat, path);
    }

    public PackAsset getAsset(String name) {
        return getAssets().stream().filter(asset -> asset.getName().equals(name)).findFirst().orElse(null);
    }

    public <T extends PackAsset> T getAsset(@NotNull Class<T> type, @NotNull String name) {
        return getAssets(type).stream().filter(asset -> asset.getName().equals(name)).findFirst().orElse(null);
    }

    public CustomTemplate getTemplate(String name) {
        return getTemplates().stream().filter(template -> template.getName().equals(name)).findFirst().orElse(null);
    }

    public CustomGenerator<?> getGenerator(String name) {
        return getGenerators().stream().filter(generator -> generator.getName().equals(name)).findFirst().orElse(null);
    }

    public <T extends CustomPackAsset> CustomGenerator<T> getGenerator(Class<T> type, String name) {
        return getGenerators(type).stream().filter(generator -> generator.getName().equals(name)).findFirst().orElse(null);
    }
}
