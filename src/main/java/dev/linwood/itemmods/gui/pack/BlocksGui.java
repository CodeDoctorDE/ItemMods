package dev.linwood.itemmods.gui.pack;

import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.gui.pack.block.BlockGui;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BlocksGui extends ListGui {
    public BlocksGui(String name) {
        super(ItemMods.getTranslationConfig().subTranslation("blocks"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(name)).getBlocks().stream()
                .filter(blockAsset -> blockAsset.getName().contains(gui.getSearchText())).map(blockAsset -> new TranslatedGuiItem(new ItemStackBuilder(
                        blockAsset.getIcon()).displayName("item")
                        .lore("actions").build()) {{
                    setRenderAction(gui -> setPlaceholders(blockAsset.getName()));
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
                    try {
                        pack.registerBlock(new BlockAsset(s));
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