package com.github.codedoctorde.itemmods.gui.pack.raw.model;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.raw.ModelAsset;
import org.bukkit.Material;

import java.util.Objects;
import java.util.function.Consumer;

public class ChooseModelGui extends ListGui {
    public ChooseModelGui(String namespace, Consumer<ModelAsset> action) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.model"), 4, (s, translation) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getModels()
                .stream().filter(modelAsset -> new PackObject(namespace, modelAsset.getName()).toString().contains(s)).map(modelAsset -> new StaticItem(new ItemStackBuilder(Material.ARMOR_STAND)
                        .displayName(new PackObject(namespace, modelAsset.getName()).toString()).lore(translation.getTranslation("actions")).build()) {{
                    setClickAction(event -> action.accept(modelAsset));
                }}).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls());
    }
}
