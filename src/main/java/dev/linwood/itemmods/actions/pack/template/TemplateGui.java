package dev.linwood.itemmods.actions.pack.template;

import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.actions.pack.ChoosePackGui;
import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class TemplateGui extends ListGui {
    public TemplateGui(@NotNull PackObject packObject) {
        this(packObject, null);
    }

    public TemplateGui(@NotNull PackObject packObject, @Nullable Consumer<InventoryClickEvent> backAction) {
        super(ItemMods.getTranslationConfig().subTranslation("template"), 4, gui -> Objects.requireNonNull(packObject.getAsset()).getCustomTemplates().stream().map(customTemplateData -> new StaticItem(Objects.requireNonNull(customTemplateData.getObject().getTemplate()).getIcon(packObject, customTemplateData)) {{
            var asset = packObject.getAsset();
            assert asset != null;
            setClickAction(event -> {
                if (event.getClick() == ClickType.DROP)
                    asset.unregisterCustomTemplate(packObject);
                else if (event.getClick() == ClickType.LEFT)
                    customTemplateData.getTemplate().openConfigGui(packObject, customTemplateData, (Player) event.getWhoClicked());
            });
        }}).toArray(GuiItem[]::new));
        var back = backAction;
        setListControls(new VerticalListControls() {{
            setBackAction(back);
            setCreateAction(event -> new ChoosePackGui(itemModsPack -> new ChooseTemplateGui(itemModsPack.getName(), customTemplate -> {
                Objects.requireNonNull(packObject.getAsset()).registerCustomTemplate(new PackObject(itemModsPack.getName(), customTemplate.getName()));
                packObject.save();
                rebuild();
                show((Player) event.getWhoClicked());
            }, backEvent -> show((Player) backEvent.getWhoClicked())).show((Player) event.getWhoClicked()), backEvent -> show((Player) backEvent.getWhoClicked())).show((Player) event.getWhoClicked()));
        }});
    }
}
