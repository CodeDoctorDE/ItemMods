package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;

public class PacksGui extends ListGui {
    public PacksGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.packs"), 4, (s, translation) ->
                ItemMods.getPackManager().getPacks().stream().filter(itemModsPack -> itemModsPack.getName().contains(s)).map(itemModsPack ->
                        new StaticItem(
                                new ItemStackBuilder(itemModsPack.getIcon()).addLore(translation.getTranslation("actions")).build()) {{
                            setClickAction(event -> {

                            });
                        }}).toArray(GuiItem[]::new));
    }
}
