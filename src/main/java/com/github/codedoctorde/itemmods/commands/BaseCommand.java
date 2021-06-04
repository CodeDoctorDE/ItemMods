package com.github.codedoctorde.itemmods.commands;

import com.github.codedoctorde.api.translations.TranslationConfig;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.KnowledgeGui;
import com.github.codedoctorde.itemmods.gui.MainGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TranslationConfig t = ItemMods.getTranslationConfig();
        if (args.length != 0) {
            sender.sendMessage(t.getTranslation("command.base.usage"));
            return true;
        }
        if (sender instanceof Player)
            if (sender.hasPermission("itemmods.admin"))
                new MainGui().show((Player) sender);
            else
                new KnowledgeGui().show((Player) sender);
        else
            sender.sendMessage(t.getTranslation("command.base.noplayer"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
