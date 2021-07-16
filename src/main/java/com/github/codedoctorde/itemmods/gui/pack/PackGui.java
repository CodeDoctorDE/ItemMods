package com.github.codedoctorde.itemmods.gui.pack;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.GuiCollection;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PackGui extends GuiCollection {
    public PackGui(String name) {
        var translation = ItemMods.getTranslationConfig().subTranslation("gui.pack");
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        if (!pack.isEditable()) {
            registerItem(4, 2, new TranslatedGuiItem(new ItemStackBuilder(Material.STRUCTURE_VOID).setDisplayName("readonly.title").setLore("readonly.description").build()));
            return;
        }
        for (PackTab value : PackTab.values()) {
            TranslatedChestGui gui = new TranslatedChestGui(translation, 4);
            gui.fillItems(0, 0, 0, 3, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            gui.fillItems(8, 0, 8, 3, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            Arrays.stream(PackTab.values()).map(packTab -> new TranslatedGuiItem(new ItemStackBuilder(packTab.getMaterial()).setDisplayName(packTab.name() + ".name")
                    .setEnchanted(packTab == value).build()) {{
                setClickAction(event -> setCurrent(packTab.ordinal()));
            }}).forEach(gui::addItem);
            gui.fillItems(1, 1, 7, 1, new StaticItem(new ItemStackBuilder(Material.AIR).build()));
            gui.fillItems(0, 0, 8, 1, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            switch (value) {
                case administration:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND).setDisplayName("items.title").build()));
                    break;
                case general:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).setDisplayName("no-item.title").setLore("no-item.description").build()));
                    break;
                case contents:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NAME_TAG).setDisplayName("name.title").setLore("name.description").build()) {{
                        setClickAction((event) -> {
                            var p = (Player) event.getWhoClicked();
                            hide(p);
                            var request = new ChatRequest(p);
                            p.sendMessage(translation.getTranslation("name.message"));
                            request.setSubmitAction(s -> {
                                pack.setName(s);
                                p.sendMessage(translation.getTranslation("name.success", s));
                            });
                        });
                    }});
                    break;
            }
            gui.fillItems(1, 2, 7, 2, new StaticItem(new ItemStackBuilder(Material.AIR).build()));
            gui.fillItems(0, 0, 8, 3, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            registerGui(gui);
        }
    }

    enum PackTab {
        general, contents, administration;

        public Material getMaterial() {
            switch (this) {
                case administration:
                    return Material.COMMAND_BLOCK;
                case general:
                    return Material.ITEM_FRAME;
                case contents:
                    return Material.BOOK;
            }
            return Material.AIR;
        }
    }
}
