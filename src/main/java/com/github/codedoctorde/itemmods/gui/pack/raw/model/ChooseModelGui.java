package com.github.codedoctorde.itemmods.gui.pack.raw.model;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.raw.ModelAsset;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class ChooseModelGui extends ListGui {
    public ChooseModelGui(String name, @NotNull Consumer<ModelAsset> action) {
        this(name, null, action);
    }

    public ChooseModelGui(String namespace, @Nullable Consumer<InventoryClickEvent> backAction, @NotNull Consumer<ModelAsset> action) {
        super(ItemMods.getTranslationConfig().subTranslation("choose.model"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getModels()
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(gui.getSearchText())).map(asset -> new TranslatedGuiItem(new ItemStackBuilder(asset.getIcon())
                        .displayName("item").lore("actions").build()) {{
                    setRenderAction(gui -> setPlaceholders(new PackObject(namespace, asset.getName()).toString()));
                    setClickAction(event -> action.accept(asset));
                }}).toArray(GuiItem[]::new));
        var back = backAction;
        setListControls(new VerticalListControls() {{
            setBackAction(back);
        }});
    }
}
