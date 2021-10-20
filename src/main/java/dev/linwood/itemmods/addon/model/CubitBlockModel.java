package dev.linwood.itemmods.addon.model;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.ModelTemplateAsset;
import dev.linwood.itemmods.pack.custom.CustomData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

public class CubitBlockModel extends ModelTemplateAsset {
    private final Translation t = ItemMods.subTranslation("addon.model.cubit_block");

    public CubitBlockModel(@NotNull String name) {
        super(name);
    }

    public CubitBlockModel(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
    }

    @Override
    public @NotNull ItemStack getIcon(String namespace) {
        return new ItemStackBuilder(Material.GRASS_BLOCK).displayName(t.getTranslation("title")).build();
    }

    public @Nullable Map<String, PackObject> getTextures(CustomData data) {
        if (data.getData() == null)
            return null;
        var textures = new HashMap<String, PackObject>();
        data.getData().getAsJsonObject().entrySet().forEach(entry -> textures.put(entry.getKey(), new PackObject(entry.getValue().getAsString())));
        return textures;
    }

    public void setTextures(CustomData data, @Nullable Map<String, PackObject> textures) {
        if (textures == null)
            data.setData(JsonNull.INSTANCE);
        else {
            var object = new JsonObject();
            textures.forEach((s, packObject) -> object.addProperty(s, packObject.toString()));
            data.setData(object);
        }
    }

    public ListGui createGui(CustomData data) {
        var loadedTextures = getTextures(data);
        if (loadedTextures == null)
            loadedTextures = new HashMap<>();
        var textures = loadedTextures;
        return new ListGui(t, gui -> textures.entrySet().stream().filter(entry -> entry.getKey().contains(gui.getSearchText()))
                .map(entry -> new StaticItem(new ItemStackBuilder(Material.PAPER).displayName(t.getTranslation("item", entry.getValue().toString())).lore(t.getTranslation("action")).build()) {{
                    setClickAction((event) -> {
                        textures.remove(entry.getKey());
                        setTextures(data, textures);
                        gui.rebuild();
                    });
                }}).toArray(GuiItem[]::new)) {{
            setListControls(new VerticalListControls() {{
                setCreateAction((event) -> {
                    event.getWhoClicked().sendMessage(t.getTranslation("create.message"));
                    new ChatRequest((Player) event.getWhoClicked()) {{
                        setSubmitAction((output) -> {

                        });
                    }};
                });
            }});
        }};
    }

    @Override
    protected String buildTemplate(String variation) {
        try {
            Paths.get(Objects.requireNonNull(getClass().getResource("cubit_block.json")).toURI());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public @NotNull Set<String> getVariations() {
        return new HashSet<>() {{
            add("default");
        }};
    }
}
