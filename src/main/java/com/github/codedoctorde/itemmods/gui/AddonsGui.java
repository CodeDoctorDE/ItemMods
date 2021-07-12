package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.itemmods.ItemMods;

/**
 * @author CodeDoctorDE
 */
public class AddonsGui extends ListGui {
    public AddonsGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.addons"),
                (s, translation) -> ItemMods.getApi().getAddons().stream().filter(addon -> addon.getName().contains(s)).map(addon -> new StaticItem(addon.getIcon()) {{
                    setClickAction(event -> {
                        if (!addon.openConfigGui())
                            event.getWhoClicked().sendMessage(translation.getTranslation("no-config"));
                    });
                }}).toArray(GuiItem[]::new));
    }
}
