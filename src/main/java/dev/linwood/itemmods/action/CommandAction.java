package dev.linwood.itemmods.action;

import org.bukkit.command.CommandSender;

public interface CommandAction {
    default boolean showGui(CommandSender sender) {
        return false;
    }

    default boolean handleCommand(CommandSender sender, String[] args) {
        return false;
    }

    default String[] tabComplete(CommandSender sender, String[] args) {
        return new String[0];
    }

    String getTranslation(String key, Object... placeholders);
}
