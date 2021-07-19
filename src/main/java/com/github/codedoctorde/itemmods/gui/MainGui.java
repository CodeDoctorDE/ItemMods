package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainGui extends TranslatedChestGui {
    public MainGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.main"), 4);
        Translation t = getTranslation();
        setPlaceholders(ItemMods.getVersion());
        var placeholder = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName(" ").build());
        fillItems(0, 0, 0, 3, placeholder);
        fillItems(8, 0, 8, 3, placeholder);
        fillItems(0, 0, getWidth() - 1, 0, placeholder);
        fillItems(0, getHeight() - 1, getWidth() - 1, getHeight() - 1, placeholder);
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PRISMARINE_CRYSTALS).displayName("reload.title").lore("reload.description").build()) {{
            setClickAction(event -> {
                Bukkit.getPluginManager().disablePlugin(ItemMods.getPlugin());
                Bukkit.getPluginManager().enablePlugin(ItemMods.getPlugin());
                event.getWhoClicked().sendMessage(t.getTranslation("reload.success"));
            });
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND).displayName("packs.title").lore("packs.description").build()) {{
            setClickAction(event -> new PacksGui().show((Player) event.getWhoClicked()));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.KNOWLEDGE_BOOK).displayName("knowledge.title").lore("knowledge.description").build()) {{
            setClickAction(event -> new KnowledgeGui().show((Player) event.getWhoClicked()));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).displayName("source.title").lore("source.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(t.getTranslation("source.link")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.MAP).displayName("support.title").lore("support.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(t.getTranslation("support.link")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BOOK).displayName("wiki.title").lore("wiki.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(t.getTranslation("wiki.link")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAINTING).displayName("crowdin.title").lore("crowdin.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(t.getTranslation("crowdin.link")));
        }});
    }
}
