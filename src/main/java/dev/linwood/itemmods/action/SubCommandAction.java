package dev.linwood.itemmods.action;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Objects;

public interface SubCommandAction extends CommandAction {
    boolean runAction(CommandSender sender, String label, String[] args);

    @Override
    default boolean handleCommand(CommandSender sender, String[] args) {
        if (args.length == 0 || Objects.equals(args[0], "help"))
            showHelp(sender);
        else if (!runAction(sender, args[0], Arrays.copyOfRange(args, 1, args.length)))
            sender.sendMessage(getTranslation("usage"));
        return true;
    }

    @Override
    default String[] tabComplete(CommandSender sender, String[] args) {
        return CommandAction.super.tabComplete(sender, args);
    }

    default void showHelp(CommandSender sender) {
        if (sender.hasPermission("itemmods.use") || !hasTranslation("user-help"))
            sender.sendMessage(getTranslation("help"));
        else
            sender.sendMessage(getTranslation("user-help"));
    }
}
