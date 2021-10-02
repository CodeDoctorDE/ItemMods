package dev.linwood.itemmods.action.pack.item;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.StaticItemAsset;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class ChooseItemGui extends ListGui {
    public ChooseItemGui(String name, @NotNull Consumer<StaticItemAsset> action) {
        this(name, null, action);
    }

    public ChooseItemGui(String namespace, @Nullable Consumer<InventoryClickEvent> backAction, @NotNull Consumer<StaticItemAsset> action) {
        super(ItemMods.getTranslationConfig().subTranslation("choose.item").merge(ItemMods.getTranslationConfig().subTranslation("gui")), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getItems()
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(gui.getSearchText())).map(asset -> new TranslatedGuiItem(new ItemStackBuilder(asset.getIcon())
                        .displayName("item").lore("action").build()) {{
                    setRenderAction(gui -> setPlaceholders(asset.getName()));
                    setClickAction(event -> action.accept(asset));
                }}).toArray(GuiItem[]::new));
        var back = backAction;
        setListControls(new VerticalListControls() {{
            setBackAction(back);
        }});
    }
}
