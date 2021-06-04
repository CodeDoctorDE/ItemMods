package com.github.codedoctorde.itemmods;

import com.github.codedoctorde.api.serializer.BlockDataTypeAdapter;
import com.github.codedoctorde.api.serializer.ItemStackTypeAdapter;
import com.github.codedoctorde.api.serializer.LocationTypeAdapter;
import com.github.codedoctorde.api.server.Version;
import com.github.codedoctorde.api.translations.TranslationConfig;
import com.github.codedoctorde.api.utils.UpdateChecker;
import com.github.codedoctorde.itemmods.addon.BaseAddon;
import com.github.codedoctorde.itemmods.api.ItemModsApi;
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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ItemMods extends JavaPlugin {
    private static ItemMods plugin;
    private final Path baseConfig = Paths.get(getDataFolder().getPath(), "config.json");
    private final Gson gson;
    private UpdateChecker updateChecker;
    private static MainConfig mainConfig;
    private BaseCommand baseCommand;
    private static ItemModsApi api;
    private static TranslationConfig translationConfig;
    private GiveItemCommand giveItemCommand;
    private static PackManager packManager;
    private Connection connection;

    public ItemMods() {
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Location.class, new LocationTypeAdapter())
                .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackTypeAdapter())
                .registerTypeHierarchyAdapter(BlockData.class, new BlockDataTypeAdapter())
                .serializeNulls()
                .setPrettyPrinting().create();
    }

    public static ItemMods getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        updateChecker = new UpdateChecker(this, 72461);
        updateChecker.getVersion(version -> Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.version", version)));
        translationConfig = new TranslationConfig(gson, new File(getDataFolder(), "translations/en.json"));
        translationConfig.setDefault(gson.fromJson(Objects.requireNonNull(getTextResource("translations/en.json")), JsonObject.class));
        translationConfig.save();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.loading"));
        try {
            PluginMetrics.runMetrics();
        }catch(Exception e){
            e.printStackTrace();
        }
        if (Version.getVersion() == Version.UNKNOWN)
            Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.compatible"));
        try {
            packManager = new PackManager();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            if (!Files.exists(baseConfig))
                Files.createFile(baseConfig);
            mainConfig = gson.fromJson(new FileReader(baseConfig.toString()), MainConfig.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            mainConfig = new MainConfig();
        }
        if (mainConfig == null)
            mainConfig = new MainConfig();
        baseCommand = new BaseCommand();
        giveItemCommand = new GiveItemCommand();
        Objects.requireNonNull(getCommand("itemmods")).setExecutor(baseCommand);
        Objects.requireNonNull(getCommand("itemmods")).setTabCompleter(baseCommand);
        Objects.requireNonNull(getCommand("giveitem")).setExecutor(giveItemCommand);
        Objects.requireNonNull(getCommand("giveitem")).setTabCompleter(giveItemCommand);
        saveBaseConfig();

        Bukkit.getPluginManager().registerEvents(new CustomItemListener(), ItemMods.getPlugin());
        Bukkit.getPluginManager().registerEvents(new CustomBlockListener(), ItemMods.getPlugin());
        api = new ItemModsApi();
        try {
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        api.registerAddon(new BaseAddon());
        if (getServer().getPluginManager().getPlugin("BetterGUI") != null && getServer().getPluginManager().isPluginEnabled("BetterGUI")) {
            ItemModifierBuilder.INSTANCE.register(BetterGuiCustomModifier::new, "customitem", "custom-item");
        }

        Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.loaded"));
    }

    public static CustomBlockManager getCustomBlockManager() {
        return getApi().getCustomBlockManager();
    }

    public static CustomItemManager getCustomItemManager() {
        return getApi().getCustomItemManager();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.unloading"));
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
        saveBaseConfig();
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getTranslation("plugin.unloaded"));
    }

    public void saveBaseConfig() {
        try {
            FileWriter writer = new FileWriter(baseConfig.toString());
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(gson.toJson(mainConfig));
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

    public BaseCommand getBaseCommand() {
        return baseCommand;
    }

    public static TranslationConfig getTranslationConfig() {
        return translationConfig;
    }

    public Gson getGson() {
        return gson;
    }

    public static MainConfig getMainConfig() {
        return mainConfig;
    }

    public static ItemModsApi getApi() {
        return api;
    }

    public static PackManager getPackManager() {
        return packManager;
    }
}
