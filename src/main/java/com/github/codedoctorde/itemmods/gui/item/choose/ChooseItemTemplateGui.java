package com.github.codedoctorde.itemmods.gui.item.choose;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplateData;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.github.codedoctorde.itemmods.gui.item.ItemGui;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author CodeDoctorDE
 */
public class ChooseItemTemplateGui extends ListGui {

    public ChooseItemTemplateGui(ItemConfig itemConfig, ItemModsAddon addon) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.item.template"), (s, translation) -> Arrays.stream(addon.getItemTemplates()).filter(itemTemplate -> itemTemplate.getName().contains(s)).map(itemTemplate -> new StaticItem(itemTemplate.createIcon(itemConfig)) {{
            setClickAction(event -> {
                if (!itemTemplate.isCompatible(itemConfig)) {
                    event.getWhoClicked().sendMessage(translation.getTranslation("template.not_compatible"));
                    return;
                }
                itemConfig.setTemplate(new CustomItemTemplateData(itemTemplate));
                ItemMods.saveBaseConfig();
                new ItemGui(itemConfig.getIdentifier()).show((Player) event.getWhoClicked());
            });
        }}).toArray(GuiItem[]::new));
    }
}
