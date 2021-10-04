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
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.StaticBlockAsset;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class BlocksAction implements TranslationCommandAction {
    private final String namespace;

    public BlocksAction(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("blocks", "gui");
    }

    @Override
    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new ListGui(getTranslationNamespace(), 4, (listGui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getBlocks().stream()
                .filter(blockAsset -> blockAsset.getName().contains(listGui.getSearchText())).map(blockAsset -> new TranslatedGuiItem(new ItemStackBuilder(
                        blockAsset.getModel() == null ? Material.GRASS_BLOCK : blockAsset.getModel().getFallbackTexture()).displayName("item")
                        .lore("action").build()) {{
                    setRenderAction(gui -> setPlaceholders(blockAsset.getName()));
                    setClickAction(event -> new BlockAction(new PackObject(namespace, blockAsset.getName())).showGui(event.getWhoClicked()));
                }}).toArray(GuiItem[]::new));
        gui.setPlaceholders(namespace);
        var pack = ItemMods.getPackManager().getPack(namespace);
        assert pack != null;
        gui.setListControls(new VerticalListControls() {{
            setBackAction(event -> new PackAction(namespace).showGui(event.getWhoClicked()));
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

    public void showChoose(CommandSender sender, @NotNull Consumer<StaticBlockAsset> action) {
        showChoose(sender, action, null);
    }

    public void showChoose(CommandSender sender, @NotNull Consumer<StaticBlockAsset> action, @Nullable Consumer<InventoryClickEvent> backAction) {
        var t = ItemMods.subTranslation("choose.block", "gui");
        if (!(sender instanceof Player)) {
            sender.sendMessage(t.getTranslation("no-player"));
            return;
        }
        var gui = new ListGui(t, 4, (listGui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getBlocks()
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(listGui.getSearchText())).map(asset -> new TranslatedGuiItem(new ItemStackBuilder(
                        asset.getModel() == null ? Material.GRASS_BLOCK : asset.getModel().getFallbackTexture()).displayName("item")
                        .lore(listGui.getTranslation().getTranslation("action", new PackObject(namespace, asset.getName()).toString())).build()) {{
                    setRenderAction(gui -> setPlaceholders(asset.getName()));
                    setClickAction(event -> action.accept(asset));
                }}).toArray(GuiItem[]::new));
        var back = backAction;
        gui.setListControls(new VerticalListControls() {{
            setBackAction(back);
        }});
        gui.show((Player) sender);
    }
}