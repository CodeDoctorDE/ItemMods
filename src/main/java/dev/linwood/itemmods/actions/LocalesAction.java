package dev.linwood.itemmods.actions;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.Gui;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.IOException;

public class LocalesAction extends CommandAction {
    public void selectLocale(String locale) {
        ItemMods.getMainConfig().setLocale(locale);
        ItemMods.saveMainConfig();
        ItemMods.reload();
    }
    public Gui buildGui() {
        var gui = new ListGui(getTranslation(), 4, (listGui) -> {
            try {
                return ItemMods.getLocales().stream().map(s ->
                        new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).setDisplayName("item").lore("actions").build()) {{
                            setRenderAction(gui -> setPlaceholders(s));
                            setClickAction(event -> {
                                selectLocale(s);
                                new MainAction().showGui((Player) event.getWhoClicked());
                            });
                        }}).toArray(GuiItem[]::new);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GuiItem[0];
        });
        gui.setListControls(new VerticalListControls() {{
            setBackAction(event -> new MainAction().showGui((Player) event.getWhoClicked()));
        }});
        return gui;
    }

    @Override
    public Translation getTranslation() {
        return ItemMods.getTranslationConfig().subTranslation("locales").merge(ItemMods.getTranslationConfig().subTranslation("gui"));
    }
}
