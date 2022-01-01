package dev.linwood.itemmods.action;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackManager;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class MainAction implements CommandAction, TranslationCommandAction, SubCommandAction {


    @Override
    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new TranslatedChestGui(getTranslationNamespace(), 4);
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
        gui.show((Player) sender);
        return true;
    }


    public void export(CommandSender sender) {
        sender.sendMessage(getTranslation("export.message"));
        try {
            PackManager.getInstance().export("default");
            sender.sendMessage(getTranslation("export.success"));
        } catch (Exception e) {
            sender.sendMessage(getTranslation("export.failed"));
            e.printStackTrace();
        }
    }

    public void reload(CommandSender sender) {
        ItemMods.reload();
        sender.sendMessage(getTranslation("reload.success"));
    }

    public void showPacks(CommandSender sender) {
        if (sender instanceof Player)
            new PacksAction().showGui(sender);
        else
            sender.sendMessage(getTranslation("no-player"));
    }

    public void showInactivePacks(CommandSender sender) {
        if (sender instanceof Player)
            new InactivePacksAction().showGui(sender);
        else
            sender.sendMessage(getTranslation("no-player"));
    }

    public void showKnowledge(CommandSender sender) {
        if (sender instanceof Player)
            new KnowledgeAction().showGui(sender);
        else
            sender.sendMessage(getTranslation("no-player"));
    }

    public void showSource(CommandSender sender) {
        sender.sendMessage(getTranslation("source.link", "https://github.com/CodeDoctorDE/ItemMods"));
    }

    public void showSupport(CommandSender sender) {
        sender.sendMessage(getTranslation("support.link", "https://go.linwood.dev/itemmods-discord"));
    }

    public void showWiki(CommandSender sender) {
        sender.sendMessage(getTranslation("wiki.link", "https://itemmods.linwood.dev"));
    }

    public void showCrowdin(CommandSender sender) {
        sender.sendMessage(getTranslation("crowdin.link", "https://linwood.crowdin.com/ItemMods"));
    }

    public void showLocales(CommandSender sender) {
        if (sender instanceof Player)
            new LocalesAction().showGui(sender);
        else
            sender.sendMessage(getTranslation("no-player"));
    }


    public void reset(CommandSender sender) {
        ItemMods.getMainConfig().clearIdentifiers();
        ItemMods.saveMainConfig();
        sender.sendMessage(getTranslation("reset.message"));
    }

    @Override
    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("main", "gui", "action");
    }

    @Override
    public boolean runAction(CommandSender sender, String label, String[] args) {
        // User actions
        switch (label) {
            case "source":
                showSource(sender);
                return true;
            case "support":
                showSupport(sender);
                return true;
            case "wiki":
                showWiki(sender);
                return true;
            case "crowdin":
                showCrowdin(sender);
                return true;
        }
        if (sender.hasPermission("itemmods.admin")) {
            switch (label) {
                case "reload":
                case "rl":
                    reload(sender);
                    return true;
                case "reset":
                case "rs":
                    reset(sender);
                    return true;
                case "locales":
                    return new LocalesAction().handleCommand(sender, args);
                case "gui":
                    showGui(sender);
                    return true;
            }
        }
        return false;
    }

    @Override
    public String[] tabComplete(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            var userCommands = new ArrayList<>(Arrays.asList("source", "crowdin", "locales", "wiki", "support"));

            if (sender.hasPermission("itemmods.admin")) {
                userCommands.addAll(Arrays.asList("reload", "reset", "rl", "rs", "gui"));
            }
            return userCommands.toArray(String[]::new);
        } else {
            var subArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0]) {
                case "locales":
                    return new LocalesAction().tabComplete(sender, subArgs);
                case "knowledge":
                    return new KnowledgeAction().tabComplete(sender, subArgs);
            }
        }
        return new String[0];
    }
}
