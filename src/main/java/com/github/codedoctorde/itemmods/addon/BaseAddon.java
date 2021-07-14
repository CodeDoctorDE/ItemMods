package com.github.codedoctorde.itemmods.addon;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.ItemModsPack;

/**
 * Default addon for elemental features
 *
 * @author CodeDoctorDE
 */
public class BaseAddon {
    ItemModsPack pack;
    Translation addonTranslation = ItemMods.getTranslationConfig().subTranslation("addon.main");

    public BaseAddon() {
        pack = new ItemModsPack("itemmods");
        pack.setIcon(new ItemStackBuilder(addonTranslation.getTranslation("icon")).build());
        pack.setDescription(addonTranslation.getTranslation("description"));

        ItemMods.getPackManager().registerPack(pack);
    }

    public ItemModsPack getPack() {
        return pack;
    }
}
