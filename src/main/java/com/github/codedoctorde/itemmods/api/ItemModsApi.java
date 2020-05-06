package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.api.block.CustomBlockManager;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplate;
import com.github.codedoctorde.itemmods.api.item.CustomItemManager;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CodeDoctorDE
 */
public class ItemModsApi {
    private final CustomItemManager customItemManager = new CustomItemManager();
    private final CustomBlockManager customBlockManager = new CustomBlockManager();
    private final List<ItemModsAddon> addons = new ArrayList<>();

    public CustomBlockManager getCustomBlockManager() {
        return customBlockManager;
    }

    public CustomItemManager getCustomItemManager() {
        return customItemManager;
    }

    public void registerAddon(ItemModsAddon addon) {
        addons.add(addon);
    }

    public List<CustomBlockTemplate> getCustomBlockTemplates() {
        List<CustomBlockTemplate> customBlockTemplates = new ArrayList<>();
        addons.stream().map(ItemModsAddon::getBlockTemplates).forEach(customBlockTemplates::addAll);
        return customBlockTemplates;
    }

    public List<CustomItemTemplate> getCustomItemTemplates() {
        List<CustomItemTemplate> customItemTemplates = new ArrayList<>();
        addons.stream().map(ItemModsAddon::getItemTemplates).forEach(customItemTemplates::addAll);
        return customItemTemplates;
    }

    public void unregisterAddon(ItemModsAddon addon) {
        addons.remove(addon);
    }

    public List<ItemModsAddon> getAddons() {
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
