package com.github.codedoctorde.itemmods.addon;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.addon.templates.item.BlockSetTemplate;
import com.github.codedoctorde.itemmods.pack.ItemModsPack;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Default addon for elemental features
 *
 * @author CodeDoctorDE
 */
public class BaseAddon extends ItemModsPack {
    @NotNull
    final Translation addonTranslation = ItemMods.getTranslationConfig().subTranslation("addon.main");

    public BaseAddon() {
        super("itemmods", false);
        setIcon(Material.EMERALD_ORE);
        setDescription(addonTranslation.getTranslation("description"));
        registerTemplate(new BlockSetTemplate());
    }
}
