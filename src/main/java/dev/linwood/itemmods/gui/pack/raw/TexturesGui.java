package dev.linwood.itemmods.gui.pack.raw;

import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.gui.pack.PackGui;
import dev.linwood.itemmods.gui.pack.raw.texture.TextureGui;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.TextureAsset;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TexturesGui extends ListGui {
    public TexturesGui(String namespace) {
        super(ItemMods.getTranslationConfig().subTranslation("raw.textures"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getTextures()
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
