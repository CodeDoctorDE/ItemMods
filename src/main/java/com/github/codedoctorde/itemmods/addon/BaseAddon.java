package com.github.codedoctorde.itemmods.addon;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.addon.templates.item.BlockSetTemplate;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Default addon for elemental features
 *
 * @author CodeDoctorDE
 */
public class BaseAddon extends ItemModsAddon {
    Translation addonTranslation = ItemMods.getTranslationConfig().subTranslation("addon.main");

    public BaseAddon() {
        super(ItemMods.getPlugin(), "itemmods");
        registerItemTemplate(new BlockSetTemplate());
    }


    @NotNull
    @Override
    public ItemStack getIcon() {
        return new ItemStackBuilder(addonTranslation.getTranslation("icon")).build();
    }

    @Override
    public boolean openConfigGui() {
        return false;
    }
}
