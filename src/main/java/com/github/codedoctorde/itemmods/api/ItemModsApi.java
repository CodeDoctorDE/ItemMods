package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.block.CustomBlockManager;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplate;
import com.github.codedoctorde.itemmods.api.item.CustomItemManager;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author CodeDoctorDE
 */
public class ItemModsApi {
    private final CustomItemManager customItemManager = new CustomItemManager();
    private final CustomBlockManager customBlockManager = new CustomBlockManager();
    private final Set<ItemModsAddon> addons = new HashSet<>();
    private final JsonObject translation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("api");

    public ItemModsApi() {
    }

    public CustomBlockManager getCustomBlockManager() {
        return customBlockManager;
    }

    public CustomItemManager getCustomItemManager() {
        return customItemManager;
    }

    public void registerAddon(ItemModsAddon addon) {
        if(getAddon(addon.getName()) != null)
            return;
        addons.add(addon);
        Bukkit.getConsoleSender().sendMessage(MessageFormat.format(translation.get("register").getAsString(), addon.getName()));
    }

    public Set<CustomBlockTemplate> getCustomBlockTemplates() {
        Set<CustomBlockTemplate> customBlockTemplates = new HashSet<>();
        addons.stream().map(addon -> addon.blockTemplates).forEach(customBlockTemplates::addAll);
        return customBlockTemplates;
    }

    public Set<CustomItemTemplate> getCustomItemTemplates() {
        Set<CustomItemTemplate> customItemTemplates = new HashSet<>();
        addons.stream().map(addon -> addon.itemTemplates).forEach(customItemTemplates::addAll);
        return customItemTemplates;
    }

    public void unregisterAddon(ItemModsAddon addon) {
        addons.remove(addon);
        Bukkit.getConsoleSender().sendMessage(MessageFormat.format(translation.get("unregister").getAsString(), addon.getName()));
    }

    public Set<ItemModsAddon> getAddons() {
        return addons;
    }

    public CustomBlockTemplate getBlockTemplate(Class<? extends CustomBlockTemplate> templateClass) {
        return getCustomBlockTemplates().stream().filter(template -> template.getClass().equals(templateClass)).findFirst().orElse(null);
    }

    public CustomBlockTemplate getBlockTemplate(String templateClass) throws ClassNotFoundException {
        return getBlockTemplate((Class<? extends CustomBlockTemplate>) Class.forName(templateClass));
    }

    public CustomItemTemplate getItemTemplate(Class<? extends CustomItemTemplate> templateClass) {
        return getCustomItemTemplates().stream().filter(template -> template.getClass().equals(templateClass)).findFirst().orElse(null);
    }

    public CustomItemTemplate getItemTemplate(String templateClass) throws ClassNotFoundException {
        return getItemTemplate((Class<? extends CustomItemTemplate>) Class.forName(templateClass));
    }

    @Nullable
    public ItemModsAddon getAddon(String name) {
        return addons.stream().findFirst().filter(addon -> addon.getName().equals(name)).orElse(null);
    }
}
