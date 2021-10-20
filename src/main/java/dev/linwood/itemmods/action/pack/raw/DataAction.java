package dev.linwood.itemmods.action.pack.raw;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.TranslationCommandAction;
import dev.linwood.itemmods.addon.simple.raw.SimpleRawAsset;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.PackAsset;
import dev.linwood.itemmods.pack.asset.raw.RawAsset;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Consumer;

public class DataAction implements TranslationCommandAction {
    private final @NotNull Class<? extends PackAsset> assetClass;
    private final @NotNull PackObject packObject;

    public DataAction(@NotNull Class<? extends PackAsset> assetClass, @NotNull PackObject packObject) {
        this.assetClass = assetClass;
        this.packObject = packObject;
    }

    @Override
    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("raw.data", "gui");
    }

    @Override
    public boolean showGui(CommandSender sender) {
        return showGui(sender, null);
    }

    public boolean showGui(CommandSender sender, @Nullable Consumer<InventoryClickEvent> backAction) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new ListGui(getTranslationNamespace(), 4);
        var current = packObject.getAssetByType(assetClass);
        assert current != null;
        if (!(current instanceof RawAsset))
            return false;
        var asset = (RawAsset) current;
        gui.setPlaceholders(new PackObject(packObject.getNamespace(), asset.getName()).toString());
        gui.setItemBuilder((listGui) -> new ArrayList<>(asset.getVariations()) {{
            remove("default");
            add(0, "default");
        }}.stream().filter(bytes -> bytes.contains(gui.getSearchText()))
                .map(variation -> new TranslatedGuiItem() {{
                    setRenderAction(gui -> {
                        var hasData = asset.getVariations().contains(variation);
                        var prefix = (hasData ? "set" : "not-set") + ".";
                        setItemStack(new ItemStackBuilder(hasData ? Material.IRON_INGOT : Material.BARRIER).displayName(prefix + "title").lore(prefix + "description").build());
                        if (hasData)
                            setPlaceholders(variation);
                    });
                    setClickAction(event -> {
                        var hasData = asset.getVariations().contains(variation);
                        if (hasData) {
                            switch (event.getClick()) {
                                case LEFT:
                                    sender.sendMessage(ItemMods.getTranslation("coming-soon"));
                                    break;
                                case DROP:
                                    if (asset instanceof SimpleRawAsset)
                                        ((SimpleRawAsset) asset).removeVariation(variation);
                                    gui.rebuild();
                            }
                        } else
                            create((Player) event.getWhoClicked(), "default", () -> showGui(sender, backAction));
                    });
                }}).toArray(GuiItem[]::new));
        gui.setListControls(new VerticalListControls() {{
            setBackAction(backAction);
            if (asset instanceof SimpleRawAsset)
                setCreateAction(event -> create((Player) event.getWhoClicked(), () -> showGui(sender, backAction)));
        }});
        gui.show((Player) sender);
        return true;
    }

    void create(@NotNull Player player, Runnable action) {
        var asset = (RawAsset) packObject.getAssetByType(assetClass);
        if (!(asset instanceof SimpleRawAsset))
            return;
        var request = new ChatRequest(player);
        player.sendMessage(getTranslation("create.variation"));
        request.setSubmitAction(s -> create(player, s, action));
    }

    void create(@NotNull Player player, String variation, Runnable action) {
        var asset = (RawAsset) packObject.getAssetByType(assetClass);
        if (!(asset instanceof SimpleRawAsset))
            return;
        var gui = new TranslatedChestGui(ItemMods.subTranslation("raw.data.create.gui"), 4);
        gui.setPlaceholders(asset.getName());
        gui.registerItem(0, 0, new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
            setClickAction(event -> showGui(player));
        }});
        gui.registerItem(3, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.BEACON).displayName("internet.title").lore("internet.description").build()) {{
            setClickAction((event) -> {
                var request = new ChatRequest(player);
                player.sendMessage(gui.getTranslation().getTranslation("internet.message"));
                player.closeInventory();
                request.setSubmitAction(s -> {
                    try {
                        ((SimpleRawAsset) asset).setData(variation, s);
                        player.sendMessage(gui.getTranslation().getTranslation("internet.success", variation, s));
                    } catch (IOException e) {
                        player.sendMessage(gui.getTranslation().getTranslation("internet.failed", variation, s));
                        e.printStackTrace();
                    } finally {
                        action.run();
                    }
                });
            });
        }});
        gui.registerItem(5, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).displayName("file.title").lore("file.description").build()) {{
            setClickAction((event) -> {
                var request = new ChatRequest(player);
                player.sendMessage(gui.getTranslation().getTranslation("file.message"));
                gui.hide(player);
                request.setSubmitAction(s -> {
                    try {
                        var path = Paths.get(ItemMods.getTempPath().toString(), FilenameUtils.getName(s));
                        ((SimpleRawAsset) asset).setData(variation, Files.readAllBytes(path));
                        player.sendMessage(gui.getTranslation().getTranslation("file.success", variation, s));
                    } catch (Exception e) {
                        player.sendMessage(gui.getTranslation().getTranslation("file.failed", variation, s));
                        e.printStackTrace();
                    } finally {
                        gui.show(player);
                    }
                });
            });
        }});
        gui.fillItems(0, 0, 8, 3, new StaticItem(ItemStackBuilder.placeholder().build()));

        gui.show(player);
    }
}
