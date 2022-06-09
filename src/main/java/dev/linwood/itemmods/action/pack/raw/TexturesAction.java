package dev.linwood.itemmods.action.pack.raw;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.TranslationCommandAction;
import dev.linwood.itemmods.action.pack.PackAction;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.TextureAsset;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class TexturesAction implements TranslationCommandAction {
    private final String namespace;

    public TexturesAction(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("raw.textures", "action", "gui");
    }

    @Override
    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new ListGui(getTranslationNamespace(), 4, (listGui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getCollection(TextureAsset.class)
                .stream().filter(textureAsset -> textureAsset.getName().contains(listGui.getSearchText())).map(textureAsset ->
                        new TranslatedGuiItem(new ItemStackBuilder(Material.ITEM_FRAME).displayName("item").lore("actions").build()) {{
                            setRenderAction(gui -> setPlaceholders(new PackObject(namespace, textureAsset.getName()).toString()));
                            setClickAction(event -> openTexture(textureAsset.getName(), sender));
                        }}).toArray(GuiItem[]::new));
        gui.setPlaceholders(namespace);
        gui.setListControls(new VerticalListControls() {{
            setBackAction(event -> new PackAction(namespace).showGui(event.getWhoClicked()));
            setCreateAction(event -> {
                var p = (Player) event.getWhoClicked();
                var request = new ChatRequest(p);
                p.sendMessage(getTranslation("create.message"));
                gui.hide(p);
                request.setSubmitAction(s -> {
                    try {
                        var pack = ItemMods.getPackManager().getPack(namespace);
                        assert pack != null;
                        pack.register(new TextureAsset(s));
                        new PackObject(namespace, s).save();
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

    private void openTexture(String name, CommandSender sender) {
        var object = new PackObject(namespace, name);
        new TextureAction(object).showGui(sender);
    }

    public void showChoose(CommandSender sender, @NotNull Consumer<TextureAsset> action) {
        showChoose(sender, action, null);
    }

    public void showChoose(CommandSender sender, @NotNull Consumer<TextureAsset> action, @Nullable Consumer<InventoryClickEvent> backAction) {
        var t = ItemMods.subTranslation("choose.texture", "action", "gui");
        if (!(sender instanceof Player)) {
            sender.sendMessage(t.getTranslation("no-player"));
            return;
        }
        var gui = new ListGui(t, 4, (listGui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getCollection(TextureAsset.class)
                .stream().filter(asset -> new PackObject(namespace, asset.getName()).toString().contains(listGui.getSearchText())).map(asset -> new TranslatedGuiItem(new ItemStackBuilder(Material.ITEM_FRAME)
                        .displayName("item").lore("actions").build()) {{
                    setRenderAction(gui -> setPlaceholders(new PackObject(namespace, asset.getName()).toString()));
                    setClickAction(event -> action.accept(asset));
                }}).toArray(GuiItem[]::new));
        var back = backAction;
        gui.setListControls(new VerticalListControls() {{
            setBackAction(back);
        }});
        gui.show((Player) sender);
    }
}
