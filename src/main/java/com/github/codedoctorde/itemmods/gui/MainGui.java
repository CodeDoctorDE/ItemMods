package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.block.BlocksGui;
import com.github.codedoctorde.itemmods.gui.item.ItemsGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainGui extends TranslatedChestGui {
    public MainGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.main"));
        Translation translation = getTranslation();
        setCloseAction(player -> ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().remove(player));
        fillItems(0, 0, 8, 0, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build()));
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PRISMARINE_CRYSTALS).setDisplayName("reload.title").addLore("reload.description").build()) {{
            setClickAction(event -> {
                Bukkit.getPluginManager().disablePlugin(ItemMods.getPlugin());
                Bukkit.getPluginManager().enablePlugin(ItemMods.getPlugin());
                event.getWhoClicked().sendMessage(translation.getTranslation("reload.success"));
            });
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND).setDisplayName("items.title").addLore("items.description").build()) {{
            setClickAction(event -> new ItemsGui().show((Player) event.getWhoClicked()));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.GRASS_BLOCK).setDisplayName("blocks.title").addLore("blocks.description").build()){{
            setClickAction(event -> new BlocksGui().createGuis().show(((Player) event.getWhoClicked())));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.KNOWLEDGE_BOOK).setDisplayName("knowledge.title").addLore("knowledge.description").build()){{
            setClickAction(event -> new KnowledgeGui().show((Player) event.getWhoClicked()));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ENDER_CHEST).setDisplayName("addons.title").addLore("addons.description").build()){{
            setClickAction(event -> new AddonsGui().show((Player) event.getWhoClicked()));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).setDisplayName("source.title").addLore("source.description").build()){{
            setClickAction(event -> event.getWhoClicked().sendMessage(translation.getTranslation("source.link")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.MAP).setDisplayName("support.title").addLore("support.description").build()){{
            setClickAction(event -> event.getWhoClicked().sendMessage(translation.getTranslation("support.link")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BOOK).setDisplayName("wiki.title").setLore("wiki.description").build()){{
            setClickAction(event -> event.getWhoClicked().sendMessage(translation.getTranslation("wiki.link")));
        }});
    }
}
