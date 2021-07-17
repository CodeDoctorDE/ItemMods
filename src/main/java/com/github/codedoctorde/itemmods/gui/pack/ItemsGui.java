package com.github.codedoctorde.itemmods.gui.pack;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.item.ItemGui;
import com.github.codedoctorde.itemmods.pack.ItemAsset;
import com.github.codedoctorde.itemmods.pack.PackObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ItemsGui extends ListGui {
    public ItemsGui(String name) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.items"), 4, (s, translation) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(name)).getItems().stream()
                .filter(itemAsset -> itemAsset.getName().contains(s)).map(itemAsset -> new StaticItem(new ItemStackBuilder(itemAsset.getModel() == null ? Material.DIAMOND_SWORD : itemAsset.getModel().getFallbackTexture()).setDisplayName(itemAsset.getName())
                        .setLore(translation.getTranslation("actions")).build()){{
                            setClickAction(event -> new ItemGui(new PackObject(name, itemAsset.getName())).show((Player) event.getWhoClicked()));
                }}).toArray(GuiItem[]::new));
        var t = getTranslation();
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        setListControls(new VerticalListControls(){{
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
                    rebuild();
                    show(p);
                });
            });
        }});
    }
}