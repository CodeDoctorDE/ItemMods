package com.github.codedoctorde.itemmods.gui.pack.template;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class ChooseTemplateGui extends ListGui {
    public ChooseTemplateGui(String namespace, @NotNull Consumer<CustomTemplate> action) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.template"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getTemplates()
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(gui.getSearchText())).map(asset -> new TranslatedGuiItem(new ItemStackBuilder(asset.getIcon())
                        .displayName("item").lore("actions").build()) {{
                    setRenderAction(gui -> setPlaceholders(new PackObject(namespace, asset.getName()).toString()));
                    setClickAction(event -> action.accept(asset));
                }}).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls());
    }
}
