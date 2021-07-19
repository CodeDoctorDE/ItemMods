package com.github.codedoctorde.itemmods.gui.pack.raw;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.asset.raw.RawAsset;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class DataGui extends ListGui {
    private final @NotNull RawAsset asset;

    public DataGui(@NotNull RawAsset asset, @NotNull Runnable action) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.raw.data"), 4, (gui) -> new ArrayList<>(asset.getVariations()) {{
            remove("default");
            add(0, "default");
        }}.stream().filter(bytes -> bytes.contains(gui.getSearchText()))
                .map(bytes -> new StaticItem(new ItemStackBuilder().build()) {{
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
                        }
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
        var request = new ChatRequest(player);
        player.sendMessage(getTranslation().getTranslation("create.message"));
        hide(player);
        request.setSubmitAction(s -> {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            try (InputStream stream = new URL(s).openStream()) {
                byte[] buffer = new byte[4096];

                while (true) {
                    int bytesRead = stream.read(buffer);
                    if (bytesRead < 0) {
                        break;
                    }
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            asset.setTexture(variation, output.toByteArray());
            player.sendMessage(getTranslation().getTranslation("create.success", variation));
            show(player);
        });
    }
}
