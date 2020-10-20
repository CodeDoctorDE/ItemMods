package com.github.codedoctorde.itemmods;

import com.github.codedoctorde.api.CodeDoctorAPI;
import com.github.codedoctorde.api.config.ObjectConfig;
import com.github.codedoctorde.api.serializer.BlockDataTypeAdapter;
import com.github.codedoctorde.api.serializer.ItemStackTypeAdapter;
import com.github.codedoctorde.api.serializer.LocationTypeAdapter;
import com.github.codedoctorde.api.server.Version;
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
import com.github.codedoctorde.itemmods.utils.CustomItemBetterGuiProperty;
import com.github.codedoctorde.itemmods.utils.PluginMetrics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.hsgamer.bettergui.builder.PropertyBuilder;
import org.bstats.bukkit.Metrics;
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

public class ItemMods extends JavaPlugin {
    public static final String version = "§bFOOD 1.5";
    private static ItemMods plugin;
    private final File baseConfig = new File(getDataFolder(), "config.json");
    private final Gson gson;
    private CodeDoctorAPI codeDoctorAPI;
    private UpdateChecker updateChecker;
    private MainConfig mainConfig;
    private BaseCommand baseCommand;
    private ItemModsApi api;
    private ObjectConfig translationConfig;
    private GiveItemCommand giveItemCommand;
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
        codeDoctorAPI = new CodeDoctorAPI(this);
        translationConfig = new ObjectConfig(gson, new File(getDataFolder(), "translations.json"));
        translationConfig.setDefault(gson.fromJson(Objects.requireNonNull(getTextResource("translations.json")), JsonObject.class));
        translationConfig.save();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getJsonObject().getAsJsonObject("plugin").get("loading").getAsString());
        PluginMetrics.runMetrics();
        if (Version.getVersion() == Version.UNKNOWN)
            Bukkit.getConsoleSender().sendMessage(translationConfig.getJsonObject().getAsJsonObject("plugin").get("compatible").getAsString());


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
            PropertyBuilder.registerItemProperty(CustomItemBetterGuiProperty::new, "customitem", "custom-item");
        }

        Bukkit.getConsoleSender().sendMessage(translationConfig.getJsonObject().getAsJsonObject("plugin").get("loaded").getAsString());
    }

    public CustomBlockManager getCustomBlockManager() {
        return getApi().getCustomBlockManager();
    }

    public CustomItemManager getCustomItemManager() {
        return getApi().getCustomItemManager();
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

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public CodeDoctorAPI getCodeDoctorAPI() {
        return codeDoctorAPI;
    }

    public ItemModsApi getApi() {
        return api;
    }
}
