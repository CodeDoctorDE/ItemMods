package com.github.codedoctorde.itemmods.gui.pack;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.ItemModsPack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ChoosePackGui extends ListGui {
    public ChoosePackGui(@NotNull Consumer<ItemModsPack> action) {
        super(ItemMods.getTranslationConfig().subTranslation("choose.pack"), 4, (gui) -> ItemMods.getPackManager().getPacks()
                .stream().filter(pack -> pack.getName().contains(gui.getSearchText())).map(pack -> new StaticItem(new ItemStackBuilder(pack.getIcon())
                        .addLore(gui.getTranslation().getTranslation("actions")).build()) {{
                    setClickAction(event -> action.accept(pack));
                }}).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls());
    }
}
