package com.github.codedoctorde.itemmods.addon;

import com.github.codedoctorde.itemmods.addon.templates.item.BlockSetTemplate;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Default addon for elemental features
 *
 * @author CodeDoctorDE
 */
public class BaseAddon extends ItemModsAddon {
    JsonObject addonTranslation = com.github.codedoctorde.itemmods.ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("addon");

    public BaseAddon() {
        registerItemTemplate(new BlockSetTemplate());
    }

    @NotNull
    @Override
    public String getName() {
        return addonTranslation.get("name").getAsString();
    }

    @NotNull
    @Override
    public ItemStack getIcon() {
        return new ItemStackBuilder(addonTranslation.getAsJsonObject("icon")).build();
    }

    @Override
    public boolean openConfigGui() {
        return false;
    }
}
