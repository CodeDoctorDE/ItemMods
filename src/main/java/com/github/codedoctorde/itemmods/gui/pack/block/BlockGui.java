package com.github.codedoctorde.itemmods.gui.pack.block;

import com.github.codedoctorde.api.ui.GuiPane;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.PackGui;
import com.github.codedoctorde.itemmods.gui.pack.item.ItemGui;
import com.github.codedoctorde.itemmods.pack.ItemAsset;
import com.github.codedoctorde.itemmods.pack.PackObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BlockGui extends ItemGui {

    public BlockGui(PackObject packObject) {
        super(packObject);
    }

    @Override
    protected void constructGuis() {
        var t = ItemMods.getTranslationConfig().subTranslation("gui.block");
        Arrays.stream(BlockTab.values()).map(blockTab -> new TranslatedChestGui(t, 4) {{
            addPane(buildTabs(blockTab.ordinal()));
            var pane = new GuiPane(7, 1);
            switch (blockTab) {
                case general:
                    pane = buildGeneralPane(this);
                    break;
                case item:
                    pane = buildItemPane(this);
                    break;
                case block:
                    pane = buildBlockPane(this);
                    break;
                case administration:
                    pane = buildAdministrationPane(this);
                    break;
            }
            addPane(1, 2, pane);
            fillItems(0, 0, 8, 3, buildPlaceholder());
        }}).forEach(this::registerGui);
    }

    @Override
    protected GuiPane buildTabs(int index) {
        var pane = new GuiPane(9, 1);

        pane.addItem(buildPlaceholder());
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
            setClickAction(event -> new PackGui(packObject.getName()).show((Player) event.getWhoClicked()));
        }});
        pane.addItem(buildPlaceholder());
        Arrays.stream(BlockTab.values()).map(blockTab -> new TranslatedGuiItem(new ItemStackBuilder(blockTab.getMaterial()).displayName(blockTab.name()).setEnchanted(index == blockTab.ordinal()).build()) {{
            setClickAction(event -> setCurrent(blockTab.ordinal()));
        }}).forEach(pane::addItem);
        pane.fillItems(0, 0, 8, 0, buildPlaceholder());
        return pane;
    }

    private GuiPane buildBlockPane(TranslatedChestGui gui) {
        GuiPane pane = new GuiPane(7, 1);
        pane.addItem(new StaticItem(new ItemStackBuilder(Material.GRASS_BLOCK).displayName("delete.title").lore("delete.description").build()));
        pane.fillItems(0, 0, 8, 1, buildEmpty());
        return pane;
    }

    @Override
    public ItemAsset getAsset() {
        return packObject.getBlock();
    }

    public enum BlockTab {
        general, item, block, administration;

        public Material getMaterial() {
            switch (this) {
                case administration:
                    return Material.COMMAND_BLOCK;
                case general:
                    return Material.ITEM_FRAME;
                case item:
                    return Material.DIAMOND_SWORD;
                case block:
                    return Material.GRASS_BLOCK;
            }
            return Material.AIR;
        }
    }
}
