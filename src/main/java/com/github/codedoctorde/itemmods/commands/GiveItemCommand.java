package com.github.codedoctorde.itemmods.commands;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CodeDoctorDE
 */
public class GiveItemCommand implements TabCompleter, CommandExecutor {
    private final JsonObject commandTranslation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("command").getAsJsonObject("give");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1 || args.length > 3) {
            commandSender.sendMessage(commandTranslation.get("usage").getAsString());
        } else {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                commandSender.sendMessage(commandTranslation.get("noplayer").getAsString());
                return true;
            }
            ItemConfig itemConfig = Main.getPlugin().getMainConfig().getItem(args[1]);
            if (itemConfig == null) {
                commandSender.sendMessage(commandTranslation.get("noitem").getAsString());
                return true;
            }
            int count = 1;
            if (args.length == 3) {
                try {
                    count = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    commandSender.sendMessage(commandTranslation.get("notnumber").getAsString());
                    return true;
                }
            }
            ItemStack itemStack = Objects.requireNonNull(itemConfig).giveItemStack();
            itemStack.setAmount(count);
            player.getInventory().addItem(itemStack);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        List<String> available = new ArrayList<>();
        if (args.length <= 0)
            return new ArrayList<>();
        if (args.length == 1)
            available.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        else if (args.length == 2) available.addAll(Main.getPlugin().getMainConfig().getItemTags());
        else if (args.length == 3) available.addAll(Arrays.asList("1", "16", "32", "64"));
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        StringUtil.copyPartialMatches(args[args.length - 1], available, completions);
        //sort the list
        Collections.sort(completions);
        return completions;
    }
}
