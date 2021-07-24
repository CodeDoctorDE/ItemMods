package com.github.codedoctorde.itemmods.gui.pack.raw.texture;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.raw.TextureAsset;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class ChooseTextureGui extends ListGui {
    public ChooseTextureGui(String namespace, @NotNull Consumer<TextureAsset> action) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.choose.texture"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getTextures()
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(gui.getSearchText())).map(asset -> new StaticItem(new ItemStackBuilder(Material.ITEM_FRAME)
                        .displayName(new PackObject(namespace, asset.getName()).toString()).lore(gui.getTranslation().getTranslation("actions")).build()) {{
                    setClickAction(event -> action.accept(asset));
                }}).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls());
    }
}
