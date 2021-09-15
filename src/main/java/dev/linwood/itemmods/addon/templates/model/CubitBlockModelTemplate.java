package dev.linwood.itemmods.addon.templates.model;

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
import dev.linwood.itemmods.pack.asset.PackAsset;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import dev.linwood.itemmods.pack.custom.CustomTemplate;
import dev.linwood.itemmods.pack.custom.CustomTemplateData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Coming soon in 2.0.0-alpha.1
 */
public class CubitBlockModelTemplate extends CustomTemplate {
    private final Translation t = ItemMods.getTranslationConfig().subTranslation("addon.model.cubit_block");

    @Override
    public @NotNull String getName() {
        return "cubit_block";
    }

    @Override
    public @NotNull ItemStack getIcon(PackObject packObject, CustomTemplateData data) {
        return new ItemStackBuilder(Material.STONE).displayName(t.getTranslation("title")).lore(
                t.getTranslation("actions")).build();
    }

    @Override
    public @NotNull ItemStack getMainIcon() {
        return new ItemStackBuilder(Material.GRASS_BLOCK).displayName(t.getTranslation("title")).build();
    }

    @Override
    public boolean isCompatible(PackObject packObject, PackAsset packAsset) {
        return packAsset instanceof ModelAsset;
    }

    public @Nullable Map<String, PackObject> getTextures(CustomTemplateData data) {
        if (data.getData() == null)
            return null;
        var textures = new HashMap<String, PackObject>();
        data.getData().getAsJsonObject().entrySet().forEach(entry -> textures.put(entry.getKey(), new PackObject(entry.getValue().getAsString())));
        return textures;
    }

    public void setTextures(CustomTemplateData data, @Nullable Map<String, PackObject> textures) {
        if (textures == null)
            data.setData(JsonNull.INSTANCE);
        else {
            var object = new JsonObject();
            textures.forEach((s, packObject) -> object.addProperty(s, packObject.toString()));
            data.setData(object);
        }
    }

    public ListGui createGui(CustomTemplateData data) {
        var loadedTextures = getTextures(data);
        if (loadedTextures == null)
            loadedTextures = new HashMap<>();
        var textures = loadedTextures;
        return new ListGui(t, gui -> textures.entrySet().stream().filter(entry -> entry.getKey().contains(gui.getSearchText()))
                .map(entry -> new StaticItem(new ItemStackBuilder(Material.PAPER).displayName(t.getTranslation("item", entry.getValue().toString())).lore(t.getTranslation("actions")).build()) {{
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
}
