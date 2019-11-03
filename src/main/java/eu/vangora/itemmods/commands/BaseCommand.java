package eu.vangora.itemmods.commands;

import com.gitlab.codedoctorde.api.ui.Gui;
import eu.vangora.itemmods.gui.KnowledgeGui;
import eu.vangora.itemmods.gui.MainGui;
import eu.vangora.itemmods.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {
    private HashMap<Player, Gui> playerGuiHashMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0) {
            sender.sendMessage(Main.getPlugin().getTranslationConfig().getString("command", "base", "usage"));
            return true;
        }
        if (sender instanceof Player)
            if (playerGuiHashMap.containsKey(sender))
                playerGuiHashMap.get(sender).open((Player) sender);
            else
                if(sender.hasPermission("itemmods.admin"))
                    new MainGui().createGui().open((Player) sender);
                else
                    new KnowledgeGui().createGui().open((Player) sender);
        else
            sender.sendMessage(Main.getPlugin().getTranslationConfig().getString("command", "base", "noplayer"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }

    public HashMap<Player, Gui> getPlayerGuiHashMap() {
        return playerGuiHashMap;
    }
}
