package com.github.codedoctorde.itemmods.utils;

import com.github.codedoctorde.itemmods.ItemMods;
import me.hsgamer.bettergui.object.Icon;
import me.hsgamer.bettergui.object.property.item.ItemProperty;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * @author CodeDoctorDE
 */
public class CustomItemBetterGuiProperty extends ItemProperty<String, String> {
    public CustomItemBetterGuiProperty(Icon icon) {
        super(icon);
    }

    @Override
    public String getParsed(Player player) {
        return parseFromString(getValue(), player);
    }

    @Override
    public ItemStack parse(Player player, ItemStack itemStack) {
        return Objects.requireNonNull(ItemMods.getPlugin().getMainConfig().getItem(getValue())).giveItemStack();
    }

    @Override
    public boolean compareWithItemStack(Player player, ItemStack itemStack) {
        throw new UnsupportedOperationException("Cannot compare using the new method");
    }
}
