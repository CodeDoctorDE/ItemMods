package dev.linwood.itemmods.actions.pack;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.ItemModsPack;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ChoosePackGui extends ListGui {
    public ChoosePackGui(@NotNull Consumer<ItemModsPack> action) {
        this(action, null);
    }

    public ChoosePackGui(@NotNull Consumer<ItemModsPack> action, @Nullable Consumer<InventoryClickEvent> backAction) {
        super(ItemMods.getTranslationConfig().subTranslation("choose.pack").merge(ItemMods.getTranslationConfig().subTranslation("gui")), 4, (gui) -> ItemMods.getPackManager().getPacks()
                .stream().filter(pack -> pack.getName().contains(gui.getSearchText())).map(pack ->
                        new TranslatedGuiItem(
                                new ItemStackBuilder(pack.getIcon()).displayName("item").lore("actions").build()) {{
                            setRenderAction(gui -> setPlaceholders(pack.getName()));
                            setClickAction(event -> action.accept(pack));
                        }}).toArray(GuiItem[]::new));
        var back = backAction;
        setListControls(new VerticalListControls() {{
            setBackAction(back);
        }});
    }
}
