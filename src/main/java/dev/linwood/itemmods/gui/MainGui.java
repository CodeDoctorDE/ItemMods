package dev.linwood.itemmods.gui;

import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainGui extends TranslatedChestGui {
    public MainGui() {
        super(ItemMods.getTranslationConfig().subTranslation("main"), 4);
        Translation t = getTranslation();
        setPlaceholders(ItemMods.getVersion());
        var placeholder = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName(" ").build());
        fillItems(0, 0, 0, 3, placeholder);
        fillItems(8, 0, 8, 3, placeholder);
        fillItems(0, 0, getWidth() - 1, 0, placeholder);
        fillItems(0, getHeight() - 1, getWidth() - 1, getHeight() - 1, placeholder);
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PRISMARINE_CRYSTALS).displayName("reload.title").lore("reload.description").build()) {{
            setClickAction(event -> {
                ItemMods.reload();
                event.getWhoClicked().sendMessage(t.getTranslation("reload.success"));
            });
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND).displayName("packs.title").lore("packs.description").build()) {{
            setClickAction(event -> new PacksGui().show((Player) event.getWhoClicked()));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.COAL).displayName("inactive-packs.title").lore("inactive-packs.description").build()) {{
            setClickAction(event -> new InactivePacksGui().show((Player) event.getWhoClicked()));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.KNOWLEDGE_BOOK).displayName("knowledge.title").lore("knowledge.description").build()) {{
            setClickAction(event -> new KnowledgeGui().show((Player) event.getWhoClicked()));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).displayName("source.title").lore("source.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(t.getTranslation("source.link", "https://github.com/CodeDoctorDE/ItemMods")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.MAP).displayName("support.title").lore("support.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(t.getTranslation("support.link", "https://discord.gg/WzcRNGF")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BOOK).displayName("wiki.title").lore("wiki.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(t.getTranslation("wiki.link", "https://itemmods.linwood.dev")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAINTING).displayName("crowdin.title").lore("crowdin.description").build()) {{
            setClickAction(event -> event.getWhoClicked().sendMessage(t.getTranslation("crowdin.link", "https://linwood.crowdin.com/ItemMods")));
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.CHEST).displayName("export.title").lore("export.description").build()) {{
            setClickAction(event -> {
                event.getWhoClicked().sendMessage(t.getTranslation("export.message"));
                try {
                    ItemMods.getPackManager().export("default");
                    event.getWhoClicked().sendMessage(t.getTranslation("export.success"));
                } catch (Exception e) {
                    event.getWhoClicked().sendMessage(t.getTranslation("export.failed"));
                    e.printStackTrace();
                }
            });
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("reset.title").lore("reset.description").build()) {{
            setClickAction(event -> {
                ItemMods.getMainConfig().clearIdentifiers();
                ItemMods.saveMainConfig();
               event.getWhoClicked().sendMessage(t.getTranslation("reset.message"));
            });
        }});
        addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ENCHANTING_TABLE).displayName("locale.title").lore("locale.description").build()) {{
            setRenderAction(gui -> setPlaceholders(ItemMods.getMainConfig().getLocale()));
            setClickAction(event -> new LocalesGui().show((Player) event.getWhoClicked()));
        }});
    }
}
