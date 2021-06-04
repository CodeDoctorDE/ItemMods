package com.github.codedoctorde.itemmods.gui.block.choose;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplateData;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.gui.block.BlockGui;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author CodeDoctorDE
 */
public class ChooseBlockTemplateGui extends ListGui {

    public ChooseBlockTemplateGui(BlockConfig blockConfig, ItemModsAddon addon) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.blocktemplate"), (s, translation) -> Arrays.stream(addon.getBlockTemplates()).filter(blockTemplate -> blockTemplate.getName().contains(s)).map(blockTemplate -> new StaticItem(blockTemplate.createIcon(blockConfig)) {{
            setClickAction(event -> {
                blockConfig.setTemplate(new CustomBlockTemplateData(blockTemplate));
                ItemMods.saveBaseConfig();
                new BlockGui(blockConfig).show((Player) event.getWhoClicked());
            });
        }}).toArray(GuiItem[]::new));
    }
}
