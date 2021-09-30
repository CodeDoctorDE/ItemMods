package dev.linwood.itemmods.action.pack.template;

import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.pack.PackAction;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.PackAsset;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class TemplateGui extends ListGui {
    public TemplateGui(@NotNull PackObject packObject, @NotNull PackAsset asset) {
        this(packObject, asset, null);
    }

    public TemplateGui(@NotNull PackObject packObject, @NotNull PackAsset asset, @Nullable Consumer<InventoryClickEvent> backAction) {
        super(ItemMods.getTranslationConfig().subTranslation("template"), 4, gui -> asset.getCustomTemplates().stream().map(customTemplateData ->
                new StaticItem(Objects.requireNonNull(customTemplateData.getObject().getTemplate()).getIcon(packObject, customTemplateData, asset)) {{
                    setClickAction(event -> {
                        if (event.getClick() == ClickType.DROP)
                            asset.unregisterCustomTemplate(packObject);
                        else if (event.getClick() == ClickType.LEFT) {
                            var action = customTemplateData.getTemplate().generateAction(packObject, customTemplateData, asset);
                            if (action != null)
                                action.showGui(event.getWhoClicked());
                        }
                    });
                }}).toArray(GuiItem[]::new));
        var back = backAction;
        setListControls(new VerticalListControls() {{
            setBackAction(back);
            setCreateAction(event -> PackAction.showChoose(itemModsPack -> new ChooseTemplateGui(itemModsPack.getName(), customTemplate -> {
                asset.registerCustomTemplate(new PackObject(itemModsPack.getName(), customTemplate.getName()));
                packObject.save();
                rebuild();
                show((Player) event.getWhoClicked());
            }, backEvent -> show((Player) backEvent.getWhoClicked())).show((Player) event.getWhoClicked()), backEvent -> show((Player) backEvent.getWhoClicked()), event.getWhoClicked()));
        }});
    }
}
