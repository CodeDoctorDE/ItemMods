package com.github.codedoctorde.itemmods.gui.pack.raw;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.PackGui;
import com.github.codedoctorde.itemmods.gui.pack.raw.model.ModelGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.raw.ModelAsset;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ModelsGui extends ListGui {
    public ModelsGui(String namespace) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.raw.models"), 4, (gui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(namespace)).getModels()
                .stream().filter(modelAsset -> modelAsset.getName().contains(gui.getSearchText())).map(modelAsset -> new TranslatedGuiItem(new ItemStackBuilder(modelAsset.getIcon()).displayName(modelAsset.getName()).lore("actions").build()) {{
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
                    var pack = ItemMods.getPackManager().getPack(namespace);
                    assert pack != null;
                    pack.registerModel(new ModelAsset(s));
                    p.sendMessage(t.getTranslation("create.success", s));
                    rebuild();
                    show(p);
                });
            });
        }});
    }
}
