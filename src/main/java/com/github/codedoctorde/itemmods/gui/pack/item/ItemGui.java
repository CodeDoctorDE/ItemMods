package com.github.codedoctorde.itemmods.gui.pack.item;

import com.github.codedoctorde.api.ui.GuiCollection;
import com.github.codedoctorde.api.ui.GuiPane;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.PackGui;
import com.github.codedoctorde.itemmods.pack.ItemAsset;
import com.github.codedoctorde.itemmods.pack.PackObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ItemGui extends GuiCollection {
    protected final PackObject packObject;

    public enum ItemTab {
        general, item, administration;

        public Material getMaterial() {
            switch (this) {
                case administration:
                    return Material.COMMAND_BLOCK;
                case general:
                    return Material.ITEM_FRAME;
                case item:
                    return Material.DIAMOND_SWORD;
            }
            return Material.AIR;
        }
    }

    public ItemGui(PackObject packObject) {
        this.packObject = packObject;
        constructGuis();
    }

    protected void constructGuis() {
        var translation = ItemMods.getTranslationConfig().subTranslation("gui.item");
        Arrays.stream(ItemTab.values()).map(itemTab -> new TranslatedChestGui(translation, 4) {{
            addPane(buildTabs(itemTab.ordinal()));
            var guiPane = new GuiPane(7, 1);
            switch (itemTab) {
                case general:
                    guiPane = buildGeneralPane();
                case item:
                    guiPane = buildItemPane();
                    break;
                case administration:
                    guiPane = buildAdministrationPane();
                    break;
            }
            addPane(1, 2, guiPane);
            fillItems(0, 0, 8, 3, buildPlaceholder());

        }}).forEach(this::registerGui);
    }

    protected GuiPane buildAdministrationPane() {
        GuiPane pane = new GuiPane(7, 1);
        pane.addItem(new StaticItem(new ItemStackBuilder(Material.BARRIER).setDisplayName("delete.title").setLore("delete.description").build()));
        pane.fillItems(0, 0, 8, 1, buildEmpty());
        return pane;
    }

    protected GuiPane buildGeneralPane() {
        GuiPane pane = new GuiPane(7, 1);
        pane.addItem(new StaticItem(new ItemStackBuilder(Material.NAME_TAG).setDisplayName("delete.title").setLore("delete.description").build()));
        pane.addItem(new StaticItem(new ItemStackBuilder(Material.ENDER_CHEST).setDisplayName("templates.title").setLore("templates.description").build()));
        return pane;
    }

    protected GuiPane buildItemPane() {
        GuiPane pane = new GuiPane(7, 1);
        pane.addItem(new StaticItem(new ItemStackBuilder(Material.DIAMOND_SWORD).setDisplayName("delete.title").setLore("delete.description").build()));
        pane.fillItems(0, 0, 8, 1, buildEmpty());
        return pane;
    }

    protected GuiPane buildTabs(int index) {
        var pane = new GuiPane(9, 1);
        pane.addItem(buildPlaceholder());
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).setDisplayName("back.title").setLore("back.description").build()) {{
            setClickAction(event -> new PackGui(packObject.getName()).show((Player) event.getWhoClicked()));
        }});
        pane.addItem(buildPlaceholder());
        Arrays.stream(ItemTab.values()).map(itemTab -> new TranslatedGuiItem(new ItemStackBuilder(itemTab.getMaterial()).setDisplayName(itemTab.name()).setEnchanted(index == itemTab.ordinal()).build()) {{
            setClickAction(event -> setCurrent(itemTab.ordinal()));
        }}).forEach(pane::addItem);
        pane.fillItems(0, 0, 8, 0, buildPlaceholder());
        return pane;
    }

    protected GuiItem buildPlaceholder() {
        return new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build());
    }

    protected GuiItem buildEmpty() {
        return new StaticItem(new ItemStackBuilder(Material.AIR).build());
    }

    public ItemAsset getAsset() {
        return packObject.getItem();
    }
}
