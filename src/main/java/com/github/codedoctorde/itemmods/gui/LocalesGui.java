package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.IOException;

public class LocalesGui extends ListGui {
    public LocalesGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.locales"), 4, (gui) -> {
            try {
                return ItemMods.getLocales().stream().map(s ->
                        new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).setDisplayName("item").lore("actions").build()) {{
                            setRenderAction(gui -> setPlaceholders(s));
                            setClickAction(event -> {
                                ItemMods.getMainConfig().setLocale(s);
                                ItemMods.saveMainConfig();
                                ItemMods.reload();
                                new MainGui().show((Player) event.getWhoClicked());
                            });
                        }}).toArray(GuiItem[]::new);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GuiItem[0];
        });
        setListControls(new VerticalListControls() {{
            setBackAction(event -> new MainGui().show((Player) event.getWhoClicked()));
        }});
    }
}
