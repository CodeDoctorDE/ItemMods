package com.github.codedoctorde.itemmods.gui.pack.template;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.ChoosePackGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TemplateGui extends ListGui {
    public TemplateGui(@NotNull PackObject packObject) {
        super(ItemMods.getTranslationConfig().subTranslation("template"), 4, gui -> Objects.requireNonNull(packObject.getAsset()).getCustomTemplates().stream().map(customTemplateData -> new StaticItem(Objects.requireNonNull(customTemplateData.getObject().getTemplate()).getIcon(customTemplateData)) {{
            var asset = packObject.getAsset();
            assert asset != null;
            setClickAction(event -> {
                if (event.getClick() == ClickType.DROP)
                    asset.unregisterCustomTemplate(packObject);
                else if (event.getClick() == ClickType.LEFT)
                    customTemplateData.getTemplate().openConfigGui(customTemplateData, (Player) event.getWhoClicked());
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
