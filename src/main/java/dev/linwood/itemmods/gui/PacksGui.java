package dev.linwood.itemmods.gui;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.gui.pack.PackGui;
import dev.linwood.itemmods.pack.ItemModsPack;
import org.bukkit.entity.Player;

public class PacksGui extends ListGui {
    public PacksGui() {
        super(ItemMods.getTranslationConfig().subTranslation("packs"), 4, (gui) ->
                ItemMods.getPackManager().getPacks().stream().map(itemModsPack ->
                        new TranslatedGuiItem(
                                new ItemStackBuilder(itemModsPack.getIcon()).displayName("item").lore("actions").build()) {{
                            setRenderAction(gui -> setPlaceholders(itemModsPack.getName()));
                            setClickAction(event -> {
                                if (itemModsPack.isEditable())
                                    new PackGui(itemModsPack.getName()).show((Player) event.getWhoClicked());
                                else
                                    event.getWhoClicked().sendMessage(getTranslation().getTranslation("readonly"));
                            });
                        }}).toArray(GuiItem[]::new));
        var t = getTranslation();
        setListControls(new VerticalListControls() {{
            setBackAction(event -> new MainGui().show((Player) event.getWhoClicked()));
            setCreateAction(event -> {
                var p = (Player) event.getWhoClicked();
                hide(p);
                var request = new ChatRequest(p);
                p.sendMessage(t.getTranslation("create.message"));
                request.setSubmitAction(s -> {
                    try {
                        ItemMods.getPackManager().registerPack(new ItemModsPack(s, true));
                        ItemMods.getPackManager().save(s);
                        p.sendMessage(t.getTranslation("create.success", s));
                        rebuild();
                    } catch (Exception e) {
                        p.sendMessage(t.getTranslation("create.failed"));
                    } finally {
                        show(p);
                    }
                });
            });
        }});
        rebuild();
    }
}
