package com.github.codedoctorde.itemmods.addon;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.addon.templates.item.BlockSetTemplate;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplate;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Default addon for elemental features
 *
 * @author CodeDoctorDE
 */
public class ItemMods implements ItemModsAddon {
    JsonObject addonTrasnlation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("addon");

    @NotNull
    @Override
    public List<CustomItemTemplate> getItemTemplates() {
        return new ArrayList<CustomItemTemplate>() {{
            add(new BlockSetTemplate());
        }};
    }

    @NotNull
    @Override
    public List<CustomBlockTemplate> getBlockTemplates() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public String getName() {
        return "ItemMods";
    }

    @NotNull
    @Override
    public ItemStack getIcon() {
        return new ItemStackBuilder(addonTrasnlation.getAsJsonObject("icon")).build();
    }

    @Override
    public Gui openConfig() {
        return null;
    }
}
