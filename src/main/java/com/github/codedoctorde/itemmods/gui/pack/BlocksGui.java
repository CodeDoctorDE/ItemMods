package com.github.codedoctorde.itemmods.gui.pack;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.block.BlockGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.BlockAsset;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BlocksGui extends ListGui {
    public BlocksGui(String name) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.blocks"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(name)).getBlocks().stream()
                .filter(itemAsset -> itemAsset.getName().contains(gui.getSearchText())).map(blockAsset -> new StaticItem(new ItemStackBuilder(
                        blockAsset.getIcon()).displayName(blockAsset.getDisplayName())
                        .lore(gui.getTranslation().getTranslation("actions")).build()) {{
                    setClickAction(event -> new BlockGui(new PackObject(name, blockAsset.getName())).show((Player) event.getWhoClicked()));
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
                    var itemAsset = new BlockAsset(s);
                    pack.registerBlock(itemAsset);
                    rebuild();
                    show(p);
                });
            });
        }});
    }
}