package dev.linwood.itemmods.gui.pack;

import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.gui.pack.item.ItemGui;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ItemsGui extends ListGui {
    public ItemsGui(String name) {
        super(ItemMods.getTranslationConfig().subTranslation("items"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(name)).getItems().stream()
                .filter(itemAsset -> itemAsset.getName().contains(gui.getSearchText())).map(itemAsset -> new TranslatedGuiItem(new ItemStackBuilder(itemAsset.getIcon()).displayName("item")
                        .lore("actions").build()) {{
                    setRenderAction(gui -> setPlaceholders(itemAsset.getName()));
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
                    try {
                        pack.registerItem(new ItemAsset(s));
                        new PackObject(pack.getName(), s).save();
                        p.sendMessage(t.getTranslation("create.success", s));
                        rebuild();
                    } catch (UnsupportedOperationException e) {
                        p.sendMessage(t.getTranslation("create.failed"));
                    } finally {
                        show(p);
                    }
                });
            });
        }});
    }
}