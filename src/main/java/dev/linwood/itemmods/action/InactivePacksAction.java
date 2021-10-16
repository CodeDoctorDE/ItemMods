package dev.linwood.itemmods.action;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InactivePacksAction implements TranslationCommandAction {
    @Override
    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new ListGui(getTranslationNamespace(), 4, (listGui) ->
                ItemMods.getPackManager().getInactivePacks().stream().map(itemModsPack ->
                        new StaticItem(
                                new ItemStackBuilder(itemModsPack.getIcon()).addLore(getTranslation("action", itemModsPack.getName())).build()) {{
                            setClickAction(event -> {
                                activatePack(event.getWhoClicked(), itemModsPack.getName());
                                listGui.rebuild();
                            });
                        }}).toArray(GuiItem[]::new));
        gui.setListControls(new VerticalListControls() {{
            setBackAction(event -> new MainAction().showGui(event.getWhoClicked()));
        }});
        gui.show((Player) sender);
        return true;
    }

    @Override
    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("inactive-packs", "gui");
    }

    public void activatePack(CommandSender sender, String pack) {
        ItemMods.getPackManager().activatePack(pack);
    }
}
