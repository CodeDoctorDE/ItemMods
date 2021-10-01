package dev.linwood.itemmods.action;

import org.bukkit.command.CommandSender;

public abstract class CommandAction {
    public boolean showGui(CommandSender sender) {
        return false;
    }

    public boolean handleCommand(CommandSender sender, String[] args) {
        return false;
    }

    public String[] tabComplete(CommandSender sender, String[] args) {
        return new String[0];
    }

    public abstract String getTranslation(String key, Object... placeholders);
}
