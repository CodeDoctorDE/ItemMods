package com.github.codedoctorde.itemmods.gui.block.choose;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.Material;

import java.util.function.Consumer;

/**
 * @author CodeDoctorDE
 */
public class ChooseBlockConfigGui extends ListGui {
    public ChooseBlockConfigGui(Consumer<BlockConfig> blockConfigEvent) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.block.config"), (s, translation) -> ItemMods.getMainConfig().getBlocks().stream().filter(blockConfig -> blockConfig.getNamespace().contains(s) || blockConfig.getName().contains(s)).map(blockConfig -> new TranslatedGuiItem(new ItemStackBuilder(Material.GRASS_BLOCK).setDisplayName("config.title").setLore("config.description").format(
                blockConfig.getDisplayName(), blockConfig.getName(), blockConfig.getNamespace()).build()) {{
            setClickAction((event) -> blockConfigEvent.accept(blockConfig));
        }}).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls(true));
    }
}
