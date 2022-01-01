package dev.linwood.itemmods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.linwood.api.item.ItemStackTypeAdapter;
import dev.linwood.api.serializer.BlockDataTypeAdapter;
import dev.linwood.api.serializer.LocationTypeAdapter;
import dev.linwood.api.server.Version;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.translations.TranslationConfig;
import dev.linwood.api.ui.Gui;
import dev.linwood.api.utils.FileUtils;
import dev.linwood.itemmods.addon.BaseAddon;
import dev.linwood.itemmods.commands.BaseCommand;
import dev.linwood.itemmods.commands.GiveItemCommand;
import dev.linwood.itemmods.config.MainConfig;
import dev.linwood.itemmods.listener.CustomBlockListener;
import dev.linwood.itemmods.listener.CustomItemListener;
import dev.linwood.itemmods.pack.PackManager;
import dev.linwood.itemmods.utils.BetterGuiCustomModifier;
import dev.linwood.itemmods.utils.PluginMetrics;
import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemMods extends JavaPlugin {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(Location.class, new LocationTypeAdapter())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackTypeAdapter())
            .registerTypeHierarchyAdapter(BlockData.class, new BlockDataTypeAdapter())
            .serializeNulls()
            .setPrettyPrinting().create();
    /**
     * The current version of the file.
     * This is used to update the file if a new version is released!
     */
    public static final int FILE_VERSION = 0;
    private static ItemMods plugin;
    private static Path mainConfigFile;
    private static MainConfig mainConfig;
    private static TranslationConfig translationConfig;
    private static Path tempPath;
    private static Path translationsPath;
    private static boolean runningOnPaper;
    private Connection connection;

    /**
     * Get the current plugin
     *
     * @return The singleton of this class created from the server
     */
    public static ItemMods getPlugin() {
        return plugin;
    }

    /**
     * Save the main config which can be got from the {@link #getMainConfig()} method
     */
    public static void saveMainConfig() {
        try {
            FileWriter writer = new FileWriter(mainConfigFile.toString());
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(GSON.toJson(mainConfig));
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Get the translation config where all translations for this plugin
     *
     * @return The translation config with all translations
     */
    public static TranslationConfig getTranslationConfig() {
        return translationConfig;
    }

    public static Translation subTranslation(String... namespaces) {
        if (namespaces.length == 0)
            return new Translation();
        var t = translationConfig.subTranslation(namespaces[0]);
        for (String namespace : namespaces) t = t.merge(translationConfig.subTranslation(namespace));
        return t;
    }

    public static String getTranslation(String key, Object... placeholders) {
        return translationConfig.getTranslation(key, placeholders);
    }

    /**
     * Get the general configuration located in plugins/ItemMods/config.json
     * Can be saved with {@link #saveMainConfig()}
     *
     * @return The main config instance
     */
    public static MainConfig getMainConfig() {
        return mainConfig;
    }

    /**
     * Get the version of the plugin
     *
     * @return The version string
     */
    public static @NotNull String getVersion() {
        return getPlugin().getDescription().getVersion();
    }

    /**
     * Get the directory located in plugins/ItemMods/temp
     *
     * @return The path of the directory
     */
    public static Path getTempPath() {
        return tempPath;
    }

    /**
     * Reload the main config which can be got from the {@link #getMainConfig()} method
     */
    public static void reloadMainConfig() {
        try {
            if (!Files.exists(mainConfigFile))
                Files.createFile(mainConfigFile);
            mainConfig = GSON.fromJson(new FileReader(mainConfigFile.toString()), MainConfig.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            mainConfig = new MainConfig();
        }
        if (mainConfig == null)
            mainConfig = new MainConfig();
    }

    /**
     * Get all supported locales
     *
     * @return A list of the supported locales
     * @throws IOException Throws if the directory can't be read
     */
    public static List<String> getLocales() throws IOException {
        return Files.walk(translationsPath).filter(Files::isRegularFile).map(path -> translationsPath.relativize(path)).map(FileUtils::getFileName).collect(Collectors.toList());
    }

    /**
     * Reload the translation config, the main config and the pack manager
     */
    public static void reload() {
        translationConfig = new TranslationConfig(GSON, Paths.get(getPlugin().getDataFolder().getAbsolutePath(), "translations", mainConfig.getLocale() + ".json").toString());
        var inputStream = getPlugin().getTextResource("translations/" + mainConfig.getLocale() + ".json");
        assert inputStream != null;
        translationConfig.setDefault(new Translation(GSON.fromJson(inputStream, JsonObject.class)));
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PackManager.getInstance().reload();
        reloadMainConfig();
    }

    /**
     * Test if the server is running on paper
     *
     * @return Returns true if the server is running on paper
     */
    public static boolean isRunningOnPaper() {
        return runningOnPaper;
    }

    @Override
    public void onEnable() {
        plugin = this;
        translationsPath = Paths.get(getDataFolder().getAbsolutePath(), "translations");
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            runningOnPaper = true;
        } catch (ClassNotFoundException e) {
            runningOnPaper = false;
        }
        try {
            Files.createDirectories(translationsPath);
            var uri = Objects.requireNonNull(getClass().getResource("/translations/")).toURI();
            Path dirPath;
            try {
                dirPath = Paths.get(uri);
            } catch (FileSystemNotFoundException e) {
                // If this is thrown, then it means that we are running the JAR directly (example: not from an IDE)
                dirPath = FileSystems.newFileSystem(uri, new HashMap<>()).getPath("/translations/");
            }

            Files.list(dirPath)
                    .forEach(p -> {
                        var output = Paths.get(getDataFolder().getAbsolutePath(), p.toString());
                        if (!Files.exists(output)) {
                            try {
                                Files.copy(p, output);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (@NotNull IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        mainConfigFile = Paths.get(getPlugin().getDataFolder().getPath(), "config.json");
        reloadMainConfig();
        translationConfig = new TranslationConfig(GSON, Paths.get(getDataFolder().getAbsolutePath(), "translations", mainConfig.getLocale() + ".json").toString());
        var inputStream = getTextResource("translations/" + mainConfig.getLocale() + ".json");
        translationConfig.setDefault(new Translation(GSON.fromJson(Objects.requireNonNull(inputStream), JsonObject.class)));
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        translationConfig.save();
        tempPath = Paths.get(getDataFolder().getAbsolutePath(), "temp");
        try {
            Files.createDirectories(tempPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        translationConfig.save();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.loading"));
        try {
            PluginMetrics.runMetrics();
        } catch (Exception e) {
            e.printStackTrace();
        }
        var version = Version.getCurrentVersion();
        if (version.isLowerThan(new Version("1.14")) || version.isBiggerThan(new Version("1.18"))) {
            Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.compatible"));
        }
        PackManager.getInstance().registerPack(new BaseAddon());

        BaseCommand baseCommand = new BaseCommand();
        GiveItemCommand giveItemCommand = new GiveItemCommand();

        Objects.requireNonNull(getCommand("itemmods")).setExecutor(baseCommand);
        Objects.requireNonNull(getCommand("itemmods")).setTabCompleter(baseCommand);
        Objects.requireNonNull(getCommand("giveitem")).setExecutor(giveItemCommand);
        Objects.requireNonNull(getCommand("giveitem")).setTabCompleter(giveItemCommand);
        saveMainConfig();

        Bukkit.getPluginManager().registerEvents(new CustomItemListener(), ItemMods.getPlugin());
        Bukkit.getPluginManager().registerEvents(new CustomBlockListener(), ItemMods.getPlugin());
        try {
            connect();
        } catch (@NotNull ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (getServer().getPluginManager().getPlugin("BetterGUI") != null && getServer().getPluginManager().isPluginEnabled("BetterGUI")) {
            ItemModifierBuilder.INSTANCE.register(BetterGuiCustomModifier::new, "customitem", "custom-item");
        }

        Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.loaded"));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.unloading"));
        Bukkit.getOnlinePlayers().stream().filter(Gui::hasGui).forEach(HumanEntity::closeInventory);
        saveMainConfig();
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.unloaded"));
    }

    /**
     * Currently not used
     */
    private void connect() throws ClassNotFoundException, SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed() || !mainConfig.getDatabaseConfig().isActive()) return;
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + mainConfig.getDatabaseConfig().getHost() + ":" + mainConfig.getDatabaseConfig().getPort() + "/" + mainConfig.getDatabaseConfig().getDatabase(),
                    mainConfig.getDatabaseConfig().getUsername(), mainConfig.getDatabaseConfig().getPassword());
        }
    }
}
