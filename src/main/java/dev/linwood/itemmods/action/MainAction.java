package dev.linwood.itemmods.action;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MainAction extends CommandAction {

    public Translation getTranslation() {
        return ItemMods.getTranslationConfig().subTranslation("main").merge(ItemMods.getTranslationConfig().subTranslation("gui"));
    }


    @Override
    public boolean showGui(CommandSender... senders) {
        if (!(senders instanceof Player[])) {
            Arrays.stream(senders).forEach(sender -> sender.sendMessage(getTranslation().getTranslation("no-player")));
            return true;
        }
        Translation t = getTranslation();
        var gui = new TranslatedChestGui(getTranslation(), 4);
        gui.setPlaceholders(ItemMods.getVersion());
        var placeholder = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName(" ").build());
        gui.fillItems(0, 0, 0, 3, placeholder);
        gui.fillItems(8, 0, 8, 3, placeholder);
        gui.fillItems(0, 0, gui.getWidth() - 1, 0, placeholder);
        gui.fillItems(0, gui.getHeight() - 1, gui.getWidth() - 1, gui.getHeight() - 1, placeholder);
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PRISMARINE_CRYSTALS).displayName("reload.title").lore("reload.description").build()) {{
            setClickAction(event -> reload(event.getWhoClicked()));
        }});
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND).displayName("packs.title").lore("packs.description").build()) {{
            setClickAction(event -> showPacks(event.getWhoClicked()));
        }});
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.COAL).displayName("inactive-packs.title").lore("inactive-packs.description").build()) {{
            setClickAction(event -> showInactivePacks(event.getWhoClicked()));
        }});
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.KNOWLEDGE_BOOK).displayName("knowledge.title").lore("knowledge.description").build()) {{
            setClickAction(event -> showKnowledge(event.getWhoClicked()));
        }});
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).displayName("source.title").lore("source.description").build()) {{
            setClickAction(event -> showSource(event.getWhoClicked()));
        }});
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.MAP).displayName("support.title").lore("support.description").build()) {{
            setClickAction(event -> showSupport(event.getWhoClicked()));
        }});
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BOOK).displayName("wiki.title").lore("wiki.description").build()) {{
            setClickAction(event -> showWiki(event.getWhoClicked()));
        }});
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAINTING).displayName("crowdin.title").lore("crowdin.description").build()) {{
            setClickAction(event -> showCrowdin(event.getWhoClicked()));
        }});
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.CHEST).displayName("export.title").lore("export.description").build()) {{
            setClickAction(event -> export(event.getWhoClicked()));
        }});
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("reset.title").lore("reset.description").build()) {{
            setClickAction(event -> reset(event.getWhoClicked()));
        }});
        gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ENCHANTING_TABLE).displayName("locale.title").lore("locale.description").build()) {{
            setRenderAction(gui -> setPlaceholders(ItemMods.getMainConfig().getLocale()));
            setClickAction(event -> showLocales(event.getWhoClicked()));
        }});
        gui.show((Player[]) senders);
        return true;
    }

    public void export(CommandSender sender) {
        var t = getTranslation();
        sender.sendMessage(t.getTranslation("export.message"));
        try {
            ItemMods.getPackManager().export("default");
            sender.sendMessage(t.getTranslation("export.success"));
        } catch (Exception e) {
            sender.sendMessage(t.getTranslation("export.failed"));
            e.printStackTrace();
        }
    }

    public void reload(CommandSender sender) {
        ItemMods.reload();
        sender.sendMessage(getTranslation().getTranslation("reload.success"));
    }

    public void showPacks(CommandSender sender) {
        if (sender instanceof Player)
            new PacksAction().showGui(sender);
        else
            sender.sendMessage(getTranslation().getTranslation("no-player"));
    }

    public void showInactivePacks(CommandSender sender) {
        if (sender instanceof Player)
            new InactivePacksAction().showGui(sender);
        else
            sender.sendMessage(getTranslation().getTranslation("no-player"));
    }

    public void showKnowledge(CommandSender sender) {
        if (sender instanceof Player)
            new KnowledgeAction().showGui(sender);
        else
            sender.sendMessage(getTranslation().getTranslation("no-player"));
    }

    public void showSource(CommandSender sender) {
        sender.sendMessage(getTranslation().getTranslation("source.link", "https://github.com/CodeDoctorDE/ItemMods"));
    }

    public void showSupport(CommandSender sender) {
        sender.sendMessage(getTranslation().getTranslation("support.link", "https://go.linwood.dev/itemmods-discord"));
    }

    public void showWiki(CommandSender sender) {
        sender.sendMessage(getTranslation().getTranslation("wiki.link", "https://itemmods.linwood.dev"));
    }

    public void showCrowdin(CommandSender sender) {
        sender.sendMessage(getTranslation().getTranslation("crowdin.link", "https://linwood.crowdin.com/ItemMods"));
    }

    public void showLocales(CommandSender sender) {
        if (sender instanceof Player)
            new LocalesAction().showGui(sender);
        else
            sender.sendMessage(getTranslation().getTranslation("no-player"));
    }


    public void reset(CommandSender sender) {
        ItemMods.getMainConfig().clearIdentifiers();
        ItemMods.saveMainConfig();
        sender.sendMessage(getTranslation().getTranslation("reset.message"));
    }
}
