package dev.linwood.itemmods.gui.pack.raw;

import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.gui.pack.PackGui;
import dev.linwood.itemmods.gui.pack.raw.model.ModelGui;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ModelsGui extends ListGui {
    public ModelsGui(String namespace) {
        super(ItemMods.getTranslationConfig().subTranslation("raw.models"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getModels()
                .stream().filter(modelAsset -> modelAsset.getName().contains(gui.getSearchText())).map(modelAsset ->
                        new TranslatedGuiItem(new ItemStackBuilder(modelAsset.getFallbackTexture()).displayName("item").lore("actions").build()) {{
                            setRenderAction(gui -> setPlaceholders(new PackObject(namespace, modelAsset.getName()).toString()));
                            setClickAction(event -> new ModelGui(new PackObject(namespace, modelAsset.getName())).show((Player) event.getWhoClicked()));
                        }}).toArray(GuiItem[]::new));
        setPlaceholders(namespace);
        var t = getTranslation();
        setListControls(new VerticalListControls() {{
            setBackAction(event -> new PackGui(namespace).show((Player) event.getWhoClicked()));
            setCreateAction(event -> {
                var p = (Player) event.getWhoClicked();
                var request = new ChatRequest(p);
                p.sendMessage(t.getTranslation("create.message"));
                hide(p);
                request.setSubmitAction(s -> {
                    try {
                        var pack = ItemMods.getPackManager().getPack(namespace);
                        assert pack != null;
                        pack.registerModel(new ModelAsset(s));
                        new PackObject(namespace, s).save();
                        p.sendMessage(t.getTranslation("create.success", s));
                        rebuild();
                    } catch (UnsupportedOperationException e) {
                        p.sendMessage(t.getTranslation("create.failed"));
                    } finally {
                        show(p);
                    }
                });
            });
        }});
    }
}
