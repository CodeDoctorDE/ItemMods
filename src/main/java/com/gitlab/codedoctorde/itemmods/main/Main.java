package com.gitlab.codedoctorde.itemmods.main;

import com.gitlab.codedoctorde.api.config.ObjectConfig;
import com.gitlab.codedoctorde.api.game.GameStateManager;
import com.gitlab.codedoctorde.api.main.CodeDoctorAPI;
import com.gitlab.codedoctorde.api.serializer.BlockDataTypeAdapter;
import com.gitlab.codedoctorde.api.serializer.ItemStackTypeAdapter;
import com.gitlab.codedoctorde.api.serializer.LocationTypeAdapter;
import com.gitlab.codedoctorde.itemmods.api.CustomBlockManager;
import com.gitlab.codedoctorde.itemmods.commands.BaseCommand;
import com.gitlab.codedoctorde.itemmods.config.MainConfig;
import com.gitlab.codedoctorde.itemmods.listener.CustomBlockListener;
import com.gitlab.codedoctorde.itemmods.listener.CustomItemListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends JavaPlugin {
    private static Main plugin;
    private File baseConfig = new File(getDataFolder(), "config.json");
    private CodeDoctorAPI api;
    public static final String version = "§bAQUA 1.2.2";
    private Gson gson;
    private MainConfig mainConfig;
    private BaseCommand baseCommand;
    private GameStateManager gameStateManager;
    private CustomBlockManager customBlockManager;
    private ObjectConfig translationConfig;
    private Connection connection;

    public static Main getPlugin() {
        return plugin;
    }

    public Main() {
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Location.class, new LocationTypeAdapter())
                .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackTypeAdapter())
                .registerTypeHierarchyAdapter(BlockData.class, new BlockDataTypeAdapter())
                .serializeNulls()
                .setPrettyPrinting().create();
    }

    @Override
    public void onEnable() {
        plugin = this;
        api = new CodeDoctorAPI(this);
        translationConfig = new ObjectConfig(gson, new File(getDataFolder(), "translations.json"));
        translationConfig.setDefault(gson.fromJson(Objects.requireNonNull(getTextResource("translations.json")), JsonObject.class));
        translationConfig.save();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getJsonObject().getAsJsonObject("plugin").get("loading").getAsString());

        try {
            if (!baseConfig.exists())
                baseConfig.createNewFile();
            mainConfig = gson.fromJson(new FileReader(baseConfig), MainConfig.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            mainConfig = new MainConfig();
        }
        if (mainConfig == null)
            mainConfig = new MainConfig();
        baseCommand = new BaseCommand();
        Objects.requireNonNull(getCommand("itemmods")).setExecutor(baseCommand);
        Objects.requireNonNull(getCommand("itemmods")).setTabCompleter(baseCommand);
        saveBaseConfig();

        Bukkit.getPluginManager().registerEvents(new CustomItemListener(), Main.getPlugin());
        Bukkit.getPluginManager().registerEvents(new CustomBlockListener(), Main.getPlugin());
        customBlockManager = new CustomBlockManager(mainConfig.getBlocks());
        try {
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage(translationConfig.getJsonObject().getAsJsonObject("plugin").get("loaded").getAsString());
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(translationConfig.getJsonObject().getAsJsonObject("plugin").get("unloading").getAsString());
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
        saveBaseConfig();
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getJsonObject().getAsJsonObject("plugin").get("unloaded").getAsString());
    }

    public void saveBaseConfig() {
        try {
            FileWriter writer = new FileWriter(baseConfig);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(gson.toJson(mainConfig));
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void connect() throws ClassNotFoundException, SQLException {
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

    public ObjectConfig getTranslationConfig() {
        return translationConfig;
    }

    public Gson getGson() {
        return gson;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public CodeDoctorAPI getApi() {
        return api;
    }

    /***
     * Update the custom block manager with the values in the configs!
     */
    public void updateCustomBlockManager() {
        customBlockManager.getBlockConfigs().clear();
        customBlockManager.getBlockConfigs().addAll(mainConfig.getBlocks());
    }

    public CustomBlockManager getCustomBlockManager() {
        return customBlockManager;
    }
}
