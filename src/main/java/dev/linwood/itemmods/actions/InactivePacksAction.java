package dev.linwood.itemmods.actions;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.Gui;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InactivePacksAction extends CommandAction {
    public Gui buildGui() {
        var t = getTranslation();
        var gui = new ListGui(t, 4, (listGui) ->
                ItemMods.getPackManager().getInactivePacks().stream().map(itemModsPack ->
                        new StaticItem(
                                new ItemStackBuilder(itemModsPack.getIcon()).addLore(t.getTranslation("actions", itemModsPack.getName())).build()) {{
                            setClickAction(event -> {
                                activatePack(event.getWhoClicked(), itemModsPack.getName());
                                listGui.rebuild();
                            });
                        }}).toArray(GuiItem[]::new));
        gui.setListControls(new VerticalListControls() {{
            setBackAction(event -> new MainAction().showGui((Player) event.getWhoClicked()));
        }});
        return gui;
    }

    @Override
    public Translation getTranslation() {
        return ItemMods.getTranslationConfig().subTranslation("inactive-packs").merge(ItemMods.getTranslationConfig().subTranslation("gui"));
    }

    public void activatePack(CommandSender sender, String pack) {
        ItemMods.getPackManager().activatePack(pack);
    }
}
