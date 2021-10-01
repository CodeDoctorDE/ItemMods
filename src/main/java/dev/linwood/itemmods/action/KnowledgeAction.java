package dev.linwood.itemmods.action;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedItem;
import dev.linwood.itemmods.ItemMods;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KnowledgeAction extends TranslationCommandAction {
    @Override
    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new TranslatedChestGui(getTranslationNamespace(), 4);
        //Translation t = getTranslation();
        gui.registerItem(4, 1, new TranslatedItem(ItemMods.getTranslationConfig().getInstance(), new ItemStackBuilder(Material.BARRIER).displayName("coming-soon").build()));
        gui.show((Player) sender);
        return true;
    }

    @Override
    protected Translation getTranslationNamespace() {
        return ItemMods.subTranslation("knowledge");
    }
}
