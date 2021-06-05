package com.github.codedoctorde.itemmods.gui.item.choose;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ItemConfig;

import java.util.function.Consumer;

/**
 * @author CodeDoctorDE
 */
public class ChooseItemConfigGui extends ListGui {

    public ChooseItemConfigGui(Consumer<ItemConfig> itemConfigEvent) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.item.config"), (s, translation) -> ItemMods.getMainConfig().getItems().stream().filter(itemConfig -> itemConfig.getNamespace().contains(s) || itemConfig.getName().contains(s)).map(itemConfig -> new StaticItem(itemConfig.getItemStack()) {{
            setClickAction(event -> itemConfigEvent.accept(itemConfig));
        }}).toArray(GuiItem[]::new));
    }
}
