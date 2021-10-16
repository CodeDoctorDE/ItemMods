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
import dev.linwood.itemmods.pack.asset.StaticItemAsset;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class ItemsAction implements TranslationCommandAction {
    final String namespace;

    public ItemsAction(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("items", "gui");
    }

    @Override
    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new ListGui(getTranslationNamespace(), 4, (listGui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getItems().stream()
                .filter(itemAsset -> itemAsset.getName().contains(listGui.getSearchText())).map(itemAsset -> new TranslatedGuiItem(new ItemStackBuilder(itemAsset.getIcon()).displayName("item")
                        .lore("action").build()) {{
                    setRenderAction(gui -> setPlaceholders(itemAsset.getName()));
                    setClickAction(event -> new ItemAction(new PackObject(namespace, itemAsset.getName())).showGui(event.getWhoClicked()));
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
                        pack.registerItem(new StaticItemAsset(s));
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

    public void showChoose(@NotNull Consumer<StaticItemAsset> action, CommandSender sender) {
        showChoose(null, action, sender);
    }

    public void showChoose(@Nullable Consumer<InventoryClickEvent> backAction, @NotNull Consumer<StaticItemAsset> action, CommandSender sender) {
        var t = ItemMods.subTranslation("choose.item", "gui");
        if (!(sender instanceof Player)) {
            sender.sendMessage(t.getTranslation("no-player"));
            return;
        }
        var gui = new ListGui(t, 4, (listGui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getItems()
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(listGui.getSearchText())).map(asset -> new TranslatedGuiItem(new ItemStackBuilder(asset.getIcon())
                        .displayName("item").lore("action").build()) {{
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