package com.gitlab.codedoctorde.itemmods.main;

import com.gitlab.codedoctorde.api.config.JsonConfiguration;
import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.game.GameStateManager;
import com.gitlab.codedoctorde.api.main.CodeDoctorAPI;
import com.gitlab.codedoctorde.api.serializer.BlockDataTypeAdapter;
import com.gitlab.codedoctorde.api.serializer.ItemStackTypeAdapter;
import com.gitlab.codedoctorde.api.serializer.LocationTypeAdapter;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
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

import java.io.*;
import java.util.Objects;

public class Main extends JavaPlugin {
    private static Main plugin;
    private File baseConfig = new File(getDataFolder(), "config.json");
    private CodeDoctorAPI api;
    private JsonConfiguration translationConfig;
    private Gson gson;
    private MainConfig mainConfig;
    private BaseCommand baseCommand;
    private GameStateManager gameStateManager;
    private CustomBlockManager customBlockManager;

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
        translationConfig = new JsonConfiguration(new File(getDataFolder(), "translations.json"));
        translationConfig.setDefaults(gson.fromJson(Objects.requireNonNull(getTextResource("translations.json")), JsonObject.class));


        try {
            translationConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(translationConfig.getString("plugin", "loading"));

        try {
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

        Bukkit.getConsoleSender().sendMessage(translationConfig.getString("plugin", "loaded"));
    }

    public static ItemStackBuilder translateItem(JsonConfigurationSection section) {
        return new ItemStackBuilder(section.getString("material")).displayName(section.getString("name"))
                .lore(section.getStringList("lore"))
                .amount((section.containsKey("amount")) ? section.getInteger("amount") : 1);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(translationConfig.getString("plugin", "unloading"));
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
        saveBaseConfig();
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getString("plugin", "unloaded"));
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

    public BaseCommand getBaseCommand() {
        return baseCommand;
    }

    public JsonConfiguration getTranslationConfig() {
        return translationConfig;
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
