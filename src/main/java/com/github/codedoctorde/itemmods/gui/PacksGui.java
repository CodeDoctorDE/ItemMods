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
        super(ItemMods.getTranslationConfig().subTranslation("gui.packs"), 4, (s, translation) ->
                ItemMods.getPackManager().getPacks().stream().filter(itemModsPack -> itemModsPack.getName().contains(s)).map(itemModsPack ->
                        new StaticItem(
                                new ItemStackBuilder(itemModsPack.getIcon()).addLore(translation.getTranslation("actions")).build()) {{
                            setClickAction(event -> new PackGui(itemModsPack.getName()).show((Player) event.getWhoClicked()));
                        }}).toArray(GuiItem[]::new));
        var t = getTranslation();
        setListControls(new VerticalListControls(){{
            setCreateAction(event -> {
                var p = (Player) event.getWhoClicked();
                hide(p);
                var request = new ChatRequest(p);
                p.sendMessage(t.getTranslation("create.message"));
                request.setSubmitAction(s -> {
                    ItemMods.getPackManager().registerPack(new ItemModsPack(s, true));
                    p.sendMessage(t.getTranslation("create.success", s));
                    rebuild();
                    show(p);
                });
            });
        }});
    }
}
