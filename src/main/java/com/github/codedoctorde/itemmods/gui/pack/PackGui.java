package com.github.codedoctorde.itemmods.gui.pack;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.GuiPane;
import com.github.codedoctorde.api.ui.template.gui.TabGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PackGui extends TabGui {
    public PackGui(String name) {
        var translation = ItemMods.getTranslationConfig().subTranslation("gui.pack");
        setTabsBuilder(integer -> {
            GuiPane pane = new GuiPane(9, 1);
            Arrays.stream(PackTab.values()).map(packTab -> new TranslatedGuiItem(new ItemStackBuilder(packTab.getMaterial()).setDisplayName(packTab.name() + ".name")
                    .setEnchanted(PackTab.values()[integer].equals(packTab)).build())).forEach(pane::addItem);
            return pane;
        });
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        if (!pack.isEditable())
            registerItem(4, 2, new TranslatedGuiItem(new ItemStackBuilder(Material.STRUCTURE_VOID).setDisplayName("readonly.title").setLore("readonly.description").build()));
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NAME_TAG).setDisplayName("name.title").setLore("name.description").build()) {{
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
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).setDisplayName("no-item.title").setLore("no-item.description").build()));
        registerItem(1, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.ENDER_CHEST).setDisplayName("item-creator.title").setLore("item-creator.description").build()));
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND).setDisplayName("Items").build()));
    }

    enum PackTab {
        administration, general, contents;

        public Material getMaterial() {
            switch (this) {
                case administration:
                    return Material.COMMAND_BLOCK;
                case general:
                    return Material.ITEM_FRAME;
                case contents:
                    return Material.CHEST;
            }
            return Material.AIR;
        }
    }
}
