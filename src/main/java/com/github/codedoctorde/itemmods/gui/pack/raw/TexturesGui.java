package com.github.codedoctorde.itemmods.gui.pack.raw;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.PackGui;
import com.github.codedoctorde.itemmods.gui.pack.raw.texture.TextureGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.raw.TextureAsset;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TexturesGui extends ListGui {
    public TexturesGui(String namespace) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.raw.textures"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getTextures()
                .stream().filter(textureAsset -> textureAsset.getName().contains(gui.getSearchText())).map(textureAsset -> new TranslatedGuiItem(new ItemStackBuilder(Material.ITEM_FRAME).displayName(textureAsset.getName()).lore("actions").build()) {{
                    setClickAction(event -> new TextureGui(new PackObject(namespace, textureAsset.getName())).show((Player) event.getWhoClicked()));
                }}).toArray(GuiItem[]::new));
        setPlaceholders(namespace);
        var t = getTranslation();
        setListControls(new VerticalListControls() {{
            setBackAction(event -> new PackGui(namespace).show((Player) event.getWhoClicked()));
            setCreateAction(event -> {
                var p = (Player) event.getWhoClicked();
                var request = new ChatRequest(p);
                p.sendMessage(t.getTranslation("create.message"));
                hide(p);
                request.setSubmitAction(s -> {
                    var pack = ItemMods.getPackManager().getPack(namespace);
                    assert pack != null;
                    pack.registerTexture(new TextureAsset(s));
                    p.sendMessage(t.getTranslation("create.success", s));
                    rebuild();
                    show(p);
                });
            });
        }});
    }
}
