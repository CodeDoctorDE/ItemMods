package dev.linwood.itemmods.commands;

import dev.linwood.api.translations.Translation;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.api.item.CustomItemManager;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CodeDoctorDE
 */
public class GiveItemCommand implements TabCompleter, CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        Translation t = ItemMods.subTranslation("command.give");
        if (!commandSender.hasPermission("itemmods.give")) {
            commandSender.sendMessage(t.getTranslation("permission"));
            return true;
        }
        if (args.length < 2 || args.length > 3) {
            commandSender.sendMessage(t.getTranslation("usage"));
        } else {
            try {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    commandSender.sendMessage(t.getTranslation("no-player"));
                    return true;
                }
                var packObject = new PackObject(args[1]);
                var itemAsset = packObject.getAsset(ItemAsset.class);
                if (itemAsset == null) {
                    commandSender.sendMessage(t.getTranslation("no-item"));
                    return true;
                }
                int count = 1;
                if (args.length == 3) {
                    try {
                        count = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        commandSender.sendMessage(t.getTranslation("no-number"));
                        return true;
                    }
                }
                var customItem = CustomItemManager.getInstance().create(packObject);
                if (customItem == null) {
                    commandSender.sendMessage(t.getTranslation("no-item"));
                    return true;
                }
                var itemStack = customItem.getItemStack();
                itemStack.setAmount(count);
                player.getInventory().addItem(itemStack);
                commandSender.sendMessage(t.getTranslation("success", player.getName(), packObject.toString()));
            } catch (UnsupportedOperationException exception) {
                commandSender.sendMessage(t.getTranslation("no-item"));
                return true;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        final List<String> completions = new ArrayList<>();
        List<String> available = new ArrayList<>();
        if (args.length <= 0)
            return new ArrayList<>();
        if (args.length == 1)
            available.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        else if (args.length == 2)
            available.addAll(ItemMods.getPackManager().getPacks().stream().flatMap(itemModsPack -> itemModsPack.getCollection(ItemAsset.class).stream().map((itemAsset -> new PackObject(itemModsPack.getName(), itemAsset.getName())))).map(PackObject::toString).collect(Collectors.toList()));
        else if (args.length == 3) available.addAll(Arrays.asList("1", "16", "32", "64"));
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        StringUtil.copyPartialMatches(args[args.length - 1], available, completions);
        //sort the list
        Collections.sort(completions);
        return completions;
    }
}
