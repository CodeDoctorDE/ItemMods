package dev.linwood.itemmods.gui.pack.template;

import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.custom.CustomTemplate;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class ChooseTemplateGui extends ListGui {
    public ChooseTemplateGui(String name, @NotNull Consumer<CustomTemplate> action) {
        this(name, action, null);
    }

    public ChooseTemplateGui(String namespace, @NotNull Consumer<CustomTemplate> action, @Nullable Consumer<InventoryClickEvent> backAction) {
        super(ItemMods.getTranslationConfig().subTranslation("choose.template"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getTemplates()
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(gui.getSearchText())).map(asset -> new TranslatedGuiItem(new ItemStackBuilder(asset.getMainIcon())
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
