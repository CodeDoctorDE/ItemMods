package com.github.codedoctorde.itemmods.gui.block.choose;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.entity.Player;

/**
 * @author CodeDoctorDE
 */
public class ChooseBlockAddonGui extends ListGui {
    public ChooseBlockAddonGui(String blockIdentifier) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.blocktemplates"), (s, translation) -> ItemMods.getApi().getAddons().stream().filter(addon -> addon.getName().contains(s)).map(addon -> new StaticItem(addon.getIcon()) {{
            setClickAction(event ->
                    new ChooseBlockTemplateGui(blockIdentifier, addon).show((Player) event.getWhoClicked()));
        }}).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls(true));
    }
}
