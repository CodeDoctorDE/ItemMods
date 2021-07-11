package com.github.codedoctorde.itemmods.gui.block.choose;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.pack.template.block.CustomBlockTemplateData;
import com.github.codedoctorde.itemmods.gui.block.BlockGui;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author CodeDoctorDE
 */
public class ChooseBlockTemplateGui extends ListGui {

    public ChooseBlockTemplateGui(String blockIdentifier, ItemModsAddon addon) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.blocktemplate"), (s, translation) -> Arrays.stream(addon.getBlockTemplates()).filter(blockTemplate -> blockTemplate.getName().contains(s)).map(blockTemplate -> new StaticItem(blockTemplate.createIcon(ItemMods.getMainConfig().getBlock(blockIdentifier))) {{
            setClickAction(event -> {
                var blockConfig = ItemMods.getMainConfig().getBlock(blockIdentifier);
                assert blockConfig != null;
                blockConfig.setTemplate(new CustomBlockTemplateData(blockTemplate));
                ItemMods.saveBaseConfig();
                new BlockGui(blockConfig).show((Player) event.getWhoClicked());
            });
        }}).toArray(GuiItem[]::new));
    }
}
