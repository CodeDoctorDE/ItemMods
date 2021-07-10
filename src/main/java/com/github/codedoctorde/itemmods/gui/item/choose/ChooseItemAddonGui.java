package com.github.codedoctorde.itemmods.gui.item.choose;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import org.bukkit.entity.Player;

/**
 * @author CodeDoctorDE
 */
public class ChooseItemAddonGui extends ListGui {

    public ChooseItemAddonGui(ItemConfig itemConfig) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.item.addon"), (s, translation) -> ItemMods.getApi().getAddons().stream().filter(addon -> addon.getName().contains(s)).map(addon -> new StaticItem(addon.getIcon()) {
            {
                setClickAction(event -> new ChooseItemTemplateGui(itemConfig, addon).show((Player) event.getWhoClicked()));
            }
        }).toArray(GuiItem[]::new));
        Translation t = getTranslation();
        setListControls(new VerticalListControls(true));
    }
}
