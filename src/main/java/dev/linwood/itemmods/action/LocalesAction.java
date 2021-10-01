package dev.linwood.itemmods.action;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class LocalesAction extends CommandAction {
    public void selectLocale(String locale) {
        ItemMods.getMainConfig().setLocale(locale);
        ItemMods.saveMainConfig();
        ItemMods.reload();
    }

    @Override
    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation().getTranslation("no-player"));
            return true;
        }
        var gui = new ListGui(getTranslation(), 4, (listGui) -> {
            try {
                return ItemMods.getLocales().stream().map(s ->
                        new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).setDisplayName("item").lore("action").build()) {{
                            setRenderAction(gui -> setPlaceholders(s));
                            setClickAction(event -> {
                                selectLocale(s);
                                new MainAction().showGui(event.getWhoClicked());
                            });
                        }}).toArray(GuiItem[]::new);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GuiItem[0];
        });
        gui.setListControls(new VerticalListControls() {{
            setBackAction(event -> new MainAction().showGui(event.getWhoClicked()));
        }});
        gui.show((Player) sender);
        return true;
    }

    @Override
    public Translation getTranslation() {
        return ItemMods.getTranslationConfig().subTranslation("locales").merge(ItemMods.getTranslationConfig().subTranslation("gui"));
    }
}
