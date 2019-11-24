package eu.vangora.itemmods.main;

import com.gitlab.codedoctorde.api.config.JsonConfiguration;
import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.game.GameStateManager;
import com.gitlab.codedoctorde.api.main.CodeDoctorAPI;
import com.gitlab.codedoctorde.api.serializer.ItemStackTypeAdapter;
import com.gitlab.codedoctorde.api.serializer.LocationTypeAdapter;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import eu.vangora.itemmods.commands.BaseCommand;
import eu.vangora.itemmods.config.MainConfig;
import eu.vangora.itemmods.listener.CustomBlockListener;
import eu.vangora.itemmods.listener.ItemListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main extends JavaPlugin {
    private static Main plugin;
    private JsonConfiguration baseConfig;
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


    @Override
    public void onEnable() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.registerTypeAdapter(Location.class, new LocationTypeAdapter());
        gsonBuilder.registerTypeAdapter(ItemStack.class, new ItemStackTypeAdapter());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        plugin = this;
        api = new CodeDoctorAPI(this);
        translationConfig = new JsonConfiguration(new File(getDataFolder(), "translations.json"));
        translationConfig.setDefaults(gson.fromJson(Objects.requireNonNull(getTextResource("translations.json")), JsonObject.class));
        baseConfig = new JsonConfiguration(new File(getDataFolder(), "config.json"));


        try {
            mainConfig = gson.fromJson(baseConfig.getElement(), MainConfig.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            mainConfig = new MainConfig();
        }
        try {
            translationConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(translationConfig.getString("plugin", "loading"));
        baseCommand = new BaseCommand();
        Objects.requireNonNull(getCommand("itemmods")).setExecutor(baseCommand);
        Objects.requireNonNull(getCommand("itemmods")).setTabCompleter(baseCommand);
        try {
            saveBaseConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onEnable();

        Bukkit.getPluginManager().registerEvents(new ItemListener(), Main.getPlugin());
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
        try {
            saveBaseConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getString("plugin", "unloaded"));
    }

    public void saveBaseConfig() throws IOException {
        baseConfig.set(gson.toJsonTree(mainConfig).getAsJsonObject());
        baseConfig.save();
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

    public void updateCustomBlockManager() {
        customBlockManager.getBlockConfigs().clear();
        customBlockManager.getBlockConfigs().addAll(mainConfig.getBlocks());
    }

    public CustomBlockManager getCustomBlockManager() {
        return customBlockManager;
    }
}
