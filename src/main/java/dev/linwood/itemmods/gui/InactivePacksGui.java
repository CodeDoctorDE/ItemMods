package dev.linwood.itemmods.gui;

import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.entity.Player;

public class InactivePacksGui extends ListGui {
    public InactivePacksGui() {
        super(ItemMods.getTranslationConfig().subTranslation("inactive-packs"), 4, (gui) ->
                ItemMods.getPackManager().getInactivePacks().stream().map(itemModsPack ->
                        new StaticItem(
                                new ItemStackBuilder(itemModsPack.getIcon()).addLore(gui.getTranslation().getTranslation("actions", itemModsPack.getName())).build()) {{
                            setClickAction(event -> {
                                ItemMods.getPackManager().activatePack(itemModsPack.getName());
                                gui.rebuild();
                            });
                        }}).toArray(GuiItem[]::new));
        var t = getTranslation();
        setListControls(new VerticalListControls() {{
            setBackAction(event -> new MainGui().show((Player) event.getWhoClicked()));
        }});
        rebuild();
    }
}
