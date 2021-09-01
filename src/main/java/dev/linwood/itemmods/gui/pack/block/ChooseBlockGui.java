package dev.linwood.itemmods.gui.pack.block;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class ChooseBlockGui extends ListGui {
    public ChooseBlockGui(String name, @NotNull Consumer<BlockAsset> action) {
        this(name, null, action);
    }

    public ChooseBlockGui(String namespace, @Nullable Consumer<InventoryClickEvent> backAction, @NotNull Consumer<BlockAsset> action) {
        super(ItemMods.getTranslationConfig().subTranslation("choose.block"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getBlocks()
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(gui.getSearchText())).map(asset -> new StaticItem(new ItemStackBuilder(
                        asset.getIcon()).displayName(asset.getDisplayName())
                        .lore(gui.getTranslation().getTranslation("actions", new PackObject(namespace, asset.getName()).toString())).build()) {{
                    setClickAction(event -> action.accept(asset));
                    setClickAction(event -> action.accept(asset));
                }}).toArray(GuiItem[]::new));
        var back = backAction;
        setListControls(new VerticalListControls() {{
            setBackAction(back);
        }});
    }
}
