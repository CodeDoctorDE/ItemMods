package dev.linwood.itemmods.action.pack;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.TranslationCommandAction;
import dev.linwood.itemmods.action.pack.block.BlockGui;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.StaticBlockAsset;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BlocksAction implements TranslationCommandAction {
    private final String name;

    public BlocksAction(String name) {
        this.name = name;
    }

    @Override
    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("blocks", "gui");
    }

    @Override
    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return true;
        }
        var gui = new ListGui(getTranslationNamespace(), 4, (listGui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(name)).getBlocks().stream()
                .filter(blockAsset -> blockAsset.getName().contains(listGui.getSearchText())).map(blockAsset -> new TranslatedGuiItem(new ItemStackBuilder(
                        blockAsset.getModel() == null ? Material.GRASS_BLOCK : blockAsset.getModel().getFallbackTexture()).displayName("item")
                        .lore("action").build()) {{
                    setRenderAction(gui -> setPlaceholders(blockAsset.getName()));
                    setClickAction(event -> new BlockGui(new PackObject(name, blockAsset.getName())).show((Player) event.getWhoClicked()));
                }}).toArray(GuiItem[]::new));
        gui.setPlaceholders(name);
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        gui.setListControls(new VerticalListControls() {{
            setBackAction(event -> new PackAction(name).showGui(event.getWhoClicked()));
            setCreateAction(event -> {
                var p = (Player) event.getWhoClicked();
                gui.hide(p);
                var request = new ChatRequest(p);
                p.sendMessage(getTranslation("create.message"));
                request.setSubmitAction(s -> {
                    try {
                        pack.registerBlock(new StaticBlockAsset(s));
                        new PackObject(pack.getName(), s).save();
                        p.sendMessage(getTranslation("create.success", s));
                        gui.rebuild();
                    } catch (UnsupportedOperationException e) {
                        p.sendMessage(getTranslation("create.failed"));
                    } finally {
                        gui.show(p);
                    }
                });
            });
        }});
        gui.show((Player) sender);
        return true;
    }
}