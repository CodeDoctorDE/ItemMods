package com.github.codedoctorde.itemmods.commands;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.itemmods.ItemMods;
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

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Translation t = ItemMods.getTranslationConfig().subTranslation("command.give");
        if (args.length < 2 || args.length > 3) {
            commandSender.sendMessage(t.getTranslation("usage"));
        } else {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                commandSender.sendMessage(t.getTranslation("noplayer"));
                return true;
            }
            ItemAsset itemAsset = ItemMods.getMainConfig().getItem(args[1]);
            if (itemAsset == null) {
                commandSender.sendMessage(t.getTranslation("noitem"));
                return true;
            }
            int count = 1;
            if (args.length == 3) {
                try {
                    count = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    commandSender.sendMessage(t.getTranslation("nonumber"));
                    return true;
                }
            }
            ItemStack itemStack = Objects.requireNonNull(itemAsset).giveItemStack();
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
        else if (args.length == 2)
            available.addAll(ItemMods.getMainConfig().getItems().stream().map(CustomConfig::getIdentifier).collect(Collectors.toList()));
        else if (args.length == 3) available.addAll(Arrays.asList("1", "16", "32", "64"));
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        StringUtil.copyPartialMatches(args[args.length - 1], available, completions);
        //sort the list
        Collections.sort(completions);
        return completions;
    }
}
