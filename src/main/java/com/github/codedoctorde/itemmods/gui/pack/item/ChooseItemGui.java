package com.github.codedoctorde.itemmods.gui.pack.item;

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

public class ChooseItemGui extends ListGui {
    public ChooseItemGui(String namespace, Consumer<BlockAsset> action) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.block"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getBlocks()
                .stream().filter(blockAsset -> new PackObject(namespace, blockAsset.getName()).toString().contains(gui.getSearchText())).map(blockAsset -> new StaticItem(new ItemStackBuilder(Material.ARMOR_STAND)
                        .displayName(new PackObject(namespace, blockAsset.getName()).toString()).lore(gui.getTranslation().getTranslation("actions")).build()) {{
                    setClickAction(event -> action.accept(blockAsset));
                }}).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls());
    }
}
