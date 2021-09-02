package dev.linwood.itemmods.gui.pack;

import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.ItemModsPack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ChoosePackGui extends ListGui {
    public ChoosePackGui(@NotNull Consumer<ItemModsPack> action) {
        super(ItemMods.getTranslationConfig().subTranslation("choose.pack"), 4, (gui) -> ItemMods.getPackManager().getPacks()
                .stream().filter(pack -> pack.getName().contains(gui.getSearchText())).map(pack ->
                        new TranslatedGuiItem(
                                new ItemStackBuilder(pack.getIcon()).displayName("item").lore("actions").build()) {{
                            setRenderAction(gui -> setPlaceholders(pack.getName()));
                            setClickAction(event -> action.accept(pack));
                        }}).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls());
    }
}
