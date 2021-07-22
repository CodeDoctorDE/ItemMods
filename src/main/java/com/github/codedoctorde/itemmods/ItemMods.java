package com.github.codedoctorde.itemmods;

import com.github.codedoctorde.api.serializer.BlockDataTypeAdapter;
import com.github.codedoctorde.api.serializer.ItemStackTypeAdapter;
import com.github.codedoctorde.api.serializer.LocationTypeAdapter;
import com.github.codedoctorde.api.server.Version;
import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.translations.TranslationConfig;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.utils.UpdateChecker;
import com.github.codedoctorde.itemmods.api.block.CustomBlockManager;
import com.github.codedoctorde.itemmods.api.item.CustomItemManager;
import com.github.codedoctorde.itemmods.commands.BaseCommand;
import com.github.codedoctorde.itemmods.commands.GiveItemCommand;
import com.github.codedoctorde.itemmods.config.MainConfig;
import com.github.codedoctorde.itemmods.listener.CustomBlockListener;
import com.github.codedoctorde.itemmods.listener.CustomItemListener;
import com.github.codedoctorde.itemmods.pack.PackManager;
import com.github.codedoctorde.itemmods.utils.BetterGuiCustomModifier;
import com.github.codedoctorde.itemmods.utils.PluginMetrics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ItemMods extends JavaPlugin {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(Location.class, new LocationTypeAdapter())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackTypeAdapter())
            .registerTypeHierarchyAdapter(BlockData.class, new BlockDataTypeAdapter())
            .serializeNulls()
            .setPrettyPrinting().create();
    private static ItemMods plugin;
    private static Path baseConfig;
    private static MainConfig mainConfig;
    private static CustomBlockManager customBlockManager;
    private static CustomItemManager customItemManager;
    private static TranslationConfig translationConfig;
    private static Path tempPath;
    private static PackManager packManager;
    private BaseCommand baseCommand;
    private Connection connection;

    public static ItemMods getPlugin() {
        return plugin;
    }

    public static CustomBlockManager getCustomBlockManager() {
        return customBlockManager;
    }

    public static CustomItemManager getCustomItemManager() {
        return customItemManager;
    }

    public static void saveBaseConfig() {
        try {
            FileWriter writer = new FileWriter(baseConfig.toString());
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(GSON.toJson(mainConfig));
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static TranslationConfig getTranslationConfig() {
        return translationConfig;
    }

    public static MainConfig getMainConfig() {
        return mainConfig;
    }

    public static PackManager getPackManager() {
        return packManager;
    }

    public static @NotNull String getVersion() {
        return getPlugin().getDescription().getVersion();
    }

    public static Path getTempPath() {
        return tempPath;
    }

    @Override
    public void onEnable() {
        plugin = this;
        UpdateChecker updateChecker = new UpdateChecker(this, 72461);
        //updateChecker.getVersion(version -> Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.version", version)));
        if (translationConfig == null) {
            translationConfig = new TranslationConfig(GSON, Paths.get(getDataFolder().getAbsolutePath(), "translations", "en.json").toString());
            try {
                translationConfig.setDefault(new Translation(GSON.fromJson(Objects.requireNonNull(getTextResource("translations/en.json")), JsonObject.class)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            translationConfig.reload();
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
        if (Version.getVersion() == Version.UNKNOWN)
            Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.compatible"));
        try {
            packManager = new PackManager();
        } catch (IOException e) {
            e.printStackTrace();
        }

        baseConfig = Paths.get(getPlugin().getDataFolder().getPath(), "config.json");
        reloadMainConfig();
        baseCommand = new BaseCommand();
        GiveItemCommand giveItemCommand = new GiveItemCommand();
        customBlockManager = new CustomBlockManager();
        customItemManager = new CustomItemManager();
        Objects.requireNonNull(getCommand("itemmods")).setExecutor(baseCommand);
        Objects.requireNonNull(getCommand("itemmods")).setTabCompleter(baseCommand);
        Objects.requireNonNull(getCommand("giveitem")).setExecutor(giveItemCommand);
        Objects.requireNonNull(getCommand("giveitem")).setTabCompleter(giveItemCommand);
        saveBaseConfig();

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

    private void reloadMainConfig() {
        try {
            if (!Files.exists(baseConfig))
                Files.createFile(baseConfig);
            mainConfig = GSON.fromJson(new FileReader(baseConfig.toString()), MainConfig.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            mainConfig = new MainConfig();
        }
        if (mainConfig == null)
            mainConfig = new MainConfig();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.unloading"));
        Bukkit.getOnlinePlayers().stream().filter(Gui::hasGui).forEach(HumanEntity::closeInventory);
        saveBaseConfig();
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.unloaded"));
    }

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

    public void reload() {
        translationConfig.reload();
        packManager.reload();
        reloadMainConfig();
    }

    public BaseCommand getBaseCommand() {
        return baseCommand;
    }

    public @NotNull Gson getGson() {
        return GSON;
    }
}
