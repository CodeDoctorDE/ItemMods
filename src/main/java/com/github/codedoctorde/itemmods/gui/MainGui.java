package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.MaterialListGui;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainGui extends TranslatedChestGui {
    public MainGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.main"));
        Translation translation = getTranslation();
        setPlaceholders(ItemMods.getVersion());
        fillItems(0, 0, getWidth() - 1, 0, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build()));
        fillItems(0, getHeight() - 1, getWidth() - 1, getHeight() - 1, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build()));
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PRISMARINE_CRYSTALS).setDisplayName("reload.title").addLore("reload.description").build()) {{
            setClickAction(event -> {
                Bukkit.getPluginManager().disablePlugin(ItemMods.getPlugin());
                Bukkit.getPluginManager().enablePlugin(ItemMods.getPlugin());
                event.getWhoClicked().sendMessage(translation.getTranslation("reload.success"));
            });
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND).setDisplayName("packs.title").addLore("packs.description").build()) {{
            setClickAction(event -> new PacksGui().show((Player) event.getWhoClicked()));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.KNOWLEDGE_BOOK).setDisplayName("knowledge.title").addLore("knowledge.description").build()) {{
            setClickAction(inventoryClickEvent -> new MaterialListGui(new Translation(), (material)->{}).show((Player) inventoryClickEvent.getWhoClicked()));
            //setClickAction(event -> new KnowledgeGui().show((Player) event.getWhoClicked()));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).setDisplayName("source.title").addLore("source.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(translation.getTranslation("source.link")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.MAP).setDisplayName("support.title").addLore("support.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(translation.getTranslation("support.link")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BOOK).setDisplayName("wiki.title").setLore("wiki.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(translation.getTranslation("wiki.link")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BOOK).setDisplayName("crowdin.title").setLore("crowdin.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(translation.getTranslation("crowdin.link")));
        }});
    }
}
