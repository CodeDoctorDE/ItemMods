package dev.linwood.itemmods.gui.pack.block;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class ChooseBlockGui extends ListGui {
    public ChooseBlockGui(String name, @NotNull Consumer<BlockAsset> action) {
        this(name, action, null);
    }

    public ChooseBlockGui(String namespace, @NotNull Consumer<BlockAsset> action, @Nullable Consumer<InventoryClickEvent> backAction) {
        super(ItemMods.getTranslationConfig().subTranslation("choose.block").merge(ItemMods.getTranslationConfig().subTranslation("gui")), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getBlocks()
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(gui.getSearchText())).map(asset -> new TranslatedGuiItem(new ItemStackBuilder(
                        asset.getModel() == null ? Material.GRASS_BLOCK : asset.getModel().getFallbackTexture()).displayName("item")
                        .lore(gui.getTranslation().getTranslation("actions", new PackObject(namespace, asset.getName()).toString())).build()) {{
                    setRenderAction(gui -> setPlaceholders(asset.getName()));
                    setClickAction(event -> action.accept(asset));
                }}).toArray(GuiItem[]::new));
        var back = backAction;
        setListControls(new VerticalListControls() {{
            setBackAction(back);
        }});
    }
}
