package com.github.codedoctorde.itemmods.gui.pack.block;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.BlockAsset;
import org.bukkit.Material;

import java.util.Objects;
import java.util.function.Consumer;

public class ChooseBlockGui extends ListGui {
    public ChooseBlockGui(String namespace, Consumer<BlockAsset> action) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.block"), 4, (s, translation) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getBlocks()
                .stream().filter(blockAsset -> new PackObject(namespace, blockAsset.getName()).toString().contains(s)).map(blockAsset -> new StaticItem(new ItemStackBuilder(Material.ARMOR_STAND)
                        .displayName(new PackObject(namespace, blockAsset.getName()).toString()).lore(translation.getTranslation("actions")).build()) {{
                    setClickAction(event -> action.accept(blockAsset));
                }}).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls());
    }
}
