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
public class BaseAddon {
    ItemModsPack pack;
    @NotNull Translation addonTranslation = ItemMods.getTranslationConfig().subTranslation("addon.main");

    public BaseAddon() {
        pack = new ItemModsPack("itemmods");
        pack.setIcon(Material.EMERALD_ORE);
        pack.setDescription(addonTranslation.getTranslation("description"));
        pack.registerTemplate(new BlockSetTemplate());

        ItemMods.getPackManager().registerPack(pack);
    }

    public ItemModsPack getPack() {
        return pack;
    }
}
