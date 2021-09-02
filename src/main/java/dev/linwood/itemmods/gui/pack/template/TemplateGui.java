package dev.linwood.itemmods.gui.pack.template;

import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.gui.pack.ChoosePackGui;
import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TemplateGui extends ListGui {
    public TemplateGui(@NotNull PackObject packObject) {
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
        setListControls(new VerticalListControls() {{
            setCreateAction(event -> new ChoosePackGui(itemModsPack -> new ChooseTemplateGui(itemModsPack.getName(), customTemplate -> {
                Objects.requireNonNull(packObject.getAsset()).registerCustomTemplate(new PackObject(itemModsPack.getName(), customTemplate.getName()));
                rebuild();
                show((Player) event.getWhoClicked());
            }).show((Player) event.getWhoClicked())).show((Player) event.getWhoClicked()));
        }});
    }
}
