package com.github.codedoctorde.itemmods.gui.item;

import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.itemmods.ItemMods;

import java.util.Objects;

/**
 * @author CodeDoctorDE
 */
public class ItemModifiersGui extends ListGui {
    public ItemModifiersGui(String name) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.item.modifiers"), 3, (s, t) -> Objects.requireNonNull(ItemMods.getMainConfig().getItem(name)).getModifiers().stream().map(data -> new StaticItem(data.getInstance().createMainIcon(ItemMods.getMainConfig().getItem(name)))).toArray(GuiItem[]::new));
        setPlaceholders(name);
    }
}
