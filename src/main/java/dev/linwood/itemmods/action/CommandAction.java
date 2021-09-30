package dev.linwood.itemmods.action;

import dev.linwood.api.translations.Translation;
import org.bukkit.command.CommandSender;

public abstract class CommandAction {
    public boolean showGui(CommandSender... senders) {
        return false;
    }

    public boolean handleCommand(CommandSender sender, String[] args) {
        return false;
    }

    public String[] tabComplete(CommandSender sender, String[] args) {
        return new String[0];
    }


    public abstract Translation getTranslation();
}
