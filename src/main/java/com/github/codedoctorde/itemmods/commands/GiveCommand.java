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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author CodeDoctorDE
 */
public class GiveCommand implements TabCompleter, CommandExecutor {
    private final JsonObject commandTranslation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("command").getAsJsonObject("give");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(commandTranslation.get("noplayer").getAsString());
        } else if (args.length < 1 || args.length > 2) {
            commandSender.sendMessage(commandTranslation.get("usage").getAsString());
        } else {
            Player player = (Player) commandSender;
            ItemConfig itemConfig = Main.getPlugin().getMainConfig().getItem(args[0]);
            int count = 1;
            if (args.length == 2) {
                try {
                    count = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    player.sendMessage(commandTranslation.get("notnumber").getAsString());
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
        if (args.length == 1) {
            available.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        } else if (args.length == 2) {
            available.addAll(Main.getPlugin().getMainConfig().getItemTags());
        }
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        StringUtil.copyPartialMatches(args[0], available, completions);
        //sort the list
        Collections.sort(completions);
        return completions;
    }
}
