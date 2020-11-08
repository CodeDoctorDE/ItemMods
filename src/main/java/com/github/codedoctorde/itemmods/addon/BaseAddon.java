package com.github.codedoctorde.itemmods.addon;

import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.addon.templates.item.BlockSetTemplate;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Default addon for elemental features
 *
 * @author CodeDoctorDE
 */
public class BaseAddon extends ItemModsAddon {
    JsonObject addonTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("addon");

    public BaseAddon() {
        super(ItemMods.getPlugin(), "itemmods");
        registerItemTemplate(new BlockSetTemplate());
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
