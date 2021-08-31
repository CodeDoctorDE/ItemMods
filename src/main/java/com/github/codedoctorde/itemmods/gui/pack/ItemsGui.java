package com.github.codedoctorde.itemmods.gui.pack;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.item.ItemGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.ItemAsset;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ItemsGui extends ListGui {
    public ItemsGui(String name) {
        super(ItemMods.getTranslationConfig().subTranslation("items"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(name)).getItems().stream()
                .filter(itemAsset -> itemAsset.getName().contains(gui.getSearchText())).map(itemAsset -> new StaticItem(new ItemStackBuilder(itemAsset.getIcon()).displayName(itemAsset.getName())
                        .lore(gui.getTranslation().getTranslation("actions", new PackObject(name, itemAsset.getName()).toString())).build()) {{
                    setClickAction(event -> new ItemGui(new PackObject(name, itemAsset.getName())).show((Player) event.getWhoClicked()));
                }}).toArray(GuiItem[]::new));
        setPlaceholders(name);
        var t = getTranslation();
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        setListControls(new VerticalListControls() {{
            setBackAction(event -> new PackGui(name).show((Player) event.getWhoClicked()));
            setCreateAction(event -> {
                var p = (Player) event.getWhoClicked();
                hide(p);
                var request = new ChatRequest(p);
                p.sendMessage(t.getTranslation("create.message"));
                request.setSubmitAction(s -> {
                    p.sendMessage(t.getTranslation("create.success", s));
                    var itemAsset = new ItemAsset(s);
                    pack.registerItem(itemAsset);
                    ItemMods.getPackManager().save(pack.getName());
                    rebuild();
                    show(p);
                });
            });
        }});
    }
}