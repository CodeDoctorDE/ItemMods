package eu.vangora.itemmods.main;

import com.gitlab.codedoctorde.api.config.JsonConfiguration;
import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.game.GameStateManager;
import com.gitlab.codedoctorde.api.main.CodeDoctorAPI;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import eu.vangora.itemmods.commands.BaseCommand;
import eu.vangora.itemmods.config.MainConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main extends JavaPlugin {
    private static Main plugin;
    private JsonConfiguration baseConfig;
    private CodeDoctorAPI api;
    private JsonConfiguration translationConfig;
    private Gson gson = new Gson();
    private MainConfig mainConfig;
    private BaseCommand baseCommand;
    private GameStateManager gameStateManager;

    public static Main getPlugin() {
        return plugin;
    }

    public static ItemStackBuilder translateItem(JsonConfigurationSection section) {
        return new ItemStackBuilder(section.getString("material")).name(section.getString("name")).lore(section.getStringList("lore"));
    }


    @Override
    public void onEnable() {
        plugin = this;
        api = new CodeDoctorAPI(this);
        translationConfig = new JsonConfiguration(new File(getDataFolder(), "translations.json"));
        translationConfig.setDefaults(gson.fromJson(Objects.requireNonNull(getTextResource("translations.json")), JsonObject.class));
        baseConfig = new JsonConfiguration(new File(getDataFolder(), "config.json"));
        baseConfig.setDefaults(baseConfig);

        mainConfig = (baseConfig != null) ? new MainConfig(baseConfig) : new MainConfig();
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
        Bukkit.getConsoleSender().sendMessage(translationConfig.getString("plugin", "loaded"));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(translationConfig.getString("plugin", "unloading"));
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
        super.onDisable();
        Bukkit.getConsoleSender().sendMessage(translationConfig.getString("plugin", "unloaded"));
    }

    public void saveBaseConfig() throws IOException {
        baseConfig.set(mainConfig.getElement().getAsJsonObject());
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
}
