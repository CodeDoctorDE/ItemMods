package dev.linwood.itemmods.gui.pack.raw;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.RawAsset;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataGui extends ListGui {
    private final @NotNull RawAsset asset;

    public DataGui(@NotNull String namespace, @NotNull RawAsset asset, @NotNull Runnable action) {
        super(ItemMods.getTranslationConfig().subTranslation("raw.data"), 4);
        setPlaceholders(new PackObject(namespace, asset.getName()).toString());
        setItemBuilder((gui) -> new ArrayList<>(asset.getVariations()) {{
            remove("default");
            add(0, "default");
        }}.stream().filter(bytes -> bytes.contains(gui.getSearchText()))
                .map(bytes -> new StaticItem() {{
                    setRenderAction(gui -> {
                        var hasData = asset.getVariations().contains(bytes);
                        var prefix = (hasData ? "has" : "not-has") + ".";
                        setItemStack(new ItemStackBuilder(hasData ? Material.IRON_INGOT : Material.BARRIER).displayName(prefix + "title").lore(prefix + "description").build());
                    });
                    setClickAction(event -> {
                        var hasData = asset.getVariations().contains(bytes);
                        if (hasData) {
                            asset.removeVariation(bytes);
                            gui.rebuild();
                        } else
                            create((Player) event.getWhoClicked(), "default");
                    });
                }}).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls() {{
            setBackAction(event -> action.run());
            setCreateAction(event -> create((Player) event.getWhoClicked()));
        }});
        this.asset = asset;
        setCloseAction(player -> action.run());
    }

    void create(@NotNull Player player) {
        var request = new ChatRequest(player);
        player.sendMessage(getTranslation().getTranslation("create.variation"));
        hide(player);
        request.setSubmitAction(s -> create(player, s));
    }

    void create(@NotNull Player player, String variation) {
        var gui = new TranslatedChestGui(ItemMods.getTranslationConfig().subTranslation("raw.data.create.gui"), 4);
        gui.setPlaceholders(asset.getName());
        gui.registerItem(0, 0, new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
            setClickAction(event -> show(player));
        }});
        gui.registerItem(3, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.BEACON).displayName("internet.title").lore("internet.description").build()) {{
            setClickAction((event) -> {
                var request = new ChatRequest(player);
                player.sendMessage(getTranslation().getTranslation("internet.message"));
                hide(player);
                request.setSubmitAction(s -> {
                    try {
                        asset.setData(variation, s);
                        player.sendMessage(getTranslation().getTranslation("internet.success", variation, s));
                    } catch (IOException e) {
                        player.sendMessage(getTranslation().getTranslation("internet.failed", variation, s));
                        e.printStackTrace();
                    } finally {
                        show(player);
                    }
                });
            });
        }});
        gui.registerItem(5, 1, new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).displayName("file.title").lore("file.description").build()) {{
            setClickAction((event) -> {
                var request = new ChatRequest(player);
                player.sendMessage(getTranslation().getTranslation("file.message"));
                hide(player);
                request.setSubmitAction(s -> {
                    try {
                        var path = Paths.get(ItemMods.getTempPath().toString(), s);
                        asset.setData(variation, Files.readAllBytes(path));
                        player.sendMessage(getTranslation().getTranslation("file.success", variation, s));
                    } catch (Exception e) {
                        player.sendMessage(getTranslation().getTranslation("file.failed", variation, s));
                        e.printStackTrace();
                    } finally {
                        show(player);
                    }
                });
            });
        }});
        gui.fillItems(0, 0, 8, 3, new StaticItem(ItemStackBuilder.placeholder().build()));

        gui.show(player);
    }
}
