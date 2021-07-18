package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.PackGui;
import com.github.codedoctorde.itemmods.pack.ItemModsPack;
import org.bukkit.entity.Player;

public class PacksGui extends ListGui {
    public PacksGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.packs"), 4, (gui) ->
                ItemMods.getPackManager().getPacks().stream().map(itemModsPack ->
                        new StaticItem(
                                new ItemStackBuilder(itemModsPack.getIcon()).addLore(gui.getTranslation().getTranslation("actions", itemModsPack.getName())).build()) {{
                            setClickAction(event -> new PackGui(itemModsPack.getName()).show((Player) event.getWhoClicked()));
                        }}).toArray(GuiItem[]::new));
        var t = getTranslation();
        setListControls(new VerticalListControls() {{
            setBackAction(event -> new MainGui().show((Player) event.getWhoClicked()));
            setCreateAction(event -> {
                var p = (Player) event.getWhoClicked();
                hide(p);
                var request = new ChatRequest(p);
                p.sendMessage(t.getTranslation("create.message"));
                request.setSubmitAction(s -> {
                    try {
                        ItemMods.getPackManager().registerPack(new ItemModsPack(s, true));
                        ItemMods.getPackManager().save(s);
                        p.sendMessage(t.getTranslation("create.success", s));
                        rebuild();
                    } catch (Exception e) {
                        p.sendMessage(t.getTranslation("create.failed"));
                    } finally {
                        show(p);
                    }
                });
            });
        }});
        rebuild();
    }
}
