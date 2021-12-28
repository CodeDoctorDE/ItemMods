package dev.linwood.itemmods.addon;

import dev.linwood.api.translations.Translation;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.addon.templates.item.BlockSetTemplate;
import dev.linwood.itemmods.pack.ItemModsPack;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Default addon for elemental features
 *
 * @author CodeDoctorDE
 */
public class BaseAddon extends ItemModsPack {
    @NotNull
    final Translation addonTranslation = ItemMods.subTranslation("addon.main");

    public BaseAddon() {
        super("itemmods", false);
        setIcon(Material.EMERALD_ORE);
        setDescription(addonTranslation.getTranslation("description"));
        registerTemplate(new BlockSetTemplate());
    }
}
