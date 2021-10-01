package dev.linwood.itemmods.action.pack;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.action.pack.item.ItemGui;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ItemsAction extends CommandAction {
    final String name;

    public ItemsAction(String name) {
        this.name = name;
    }

    @Override
    public Translation getTranslation() {
        return ItemMods.getTranslationConfig().subTranslation("items").merge(ItemMods.getTranslationConfig().subTranslation("gui"));
    }

    public boolean showGui(CommandSender sender) {
        var gui = new ListGui(getTranslation(), 4, (listGui) -> Objects.requireNonNull(ItemMods.getPackManager().getPack(name)).getItems().stream()
                .filter(itemAsset -> itemAsset.getName().contains(listGui.getSearchText())).map(itemAsset -> new TranslatedGuiItem(new ItemStackBuilder(itemAsset.getIcon()).displayName("item")
                        .lore("action").build()) {{
                    setRenderAction(gui -> setPlaceholders(itemAsset.getName()));
                    setClickAction(event -> new ItemGui(new PackObject(name, itemAsset.getName())).show((Player) event.getWhoClicked()));
                }}).toArray(GuiItem[]::new));
        gui.setPlaceholders(name);
        var t = getTranslation();
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        gui.setListControls(new VerticalListControls() {{
            setBackAction(event -> new PackAction(name).showGui(event.getWhoClicked()));
            setCreateAction(event -> {
                var p = (Player) event.getWhoClicked();
                gui.hide(p);
                var request = new ChatRequest(p);
                p.sendMessage(t.getTranslation("create.message"));
                request.setSubmitAction(s -> {
                    try {
                        pack.registerItem(new ItemAsset(s));
                        new PackObject(pack.getName(), s).save();
                        p.sendMessage(t.getTranslation("create.success", s));
                        gui.rebuild();
                    } catch (UnsupportedOperationException e) {
                        p.sendMessage(t.getTranslation("create.failed"));
                    } finally {
                        gui.show(p);
                    }
                });
            });
        }});
        gui.show((Player) sender);
        return true;
    }
}