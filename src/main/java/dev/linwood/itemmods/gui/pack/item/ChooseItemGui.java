package dev.linwood.itemmods.gui.pack.item;

import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class ChooseItemGui extends ListGui {
    public ChooseItemGui(String name, @NotNull Consumer<ItemAsset> action) {
        this(name, null, action);
    }

    public ChooseItemGui(String namespace, @Nullable Consumer<InventoryClickEvent> backAction, @NotNull Consumer<ItemAsset> action) {
        super(ItemMods.getTranslationConfig().subTranslation("choose.item"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getItems()
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(gui.getSearchText())).map(asset -> new StaticItem(new ItemStackBuilder(asset.getIcon()).displayName(asset.getDisplayName())
                        .lore(gui.getTranslation().getTranslation("actions", new PackObject(namespace, asset.getName()).toString())).build()) {{
                    setClickAction(event -> action.accept(asset));
                }}).toArray(GuiItem[]::new));
        var back = backAction;
        setListControls(new VerticalListControls() {{
            setBackAction(back);
        }});
    }
}
