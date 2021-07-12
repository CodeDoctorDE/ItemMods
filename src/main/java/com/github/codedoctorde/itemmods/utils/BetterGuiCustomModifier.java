package com.github.codedoctorde.itemmods.utils;

import com.github.codedoctorde.itemmods.ItemMods;
import me.hsgamer.bettergui.lib.core.bukkit.item.ItemModifier;
import me.hsgamer.bettergui.lib.core.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author CodeDoctorDE
 */
public class BetterGuiCustomModifier implements ItemModifier {
    private String value = "";

    @Override
    public String getName() {
        return "customitem";
    }

    @Override
    public ItemStack modify(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> map) {
        return Objects.requireNonNull(ItemMods.getMainConfig().getItem(StringReplacer.replace(value, uuid, map.values()))).giveItemStack();
    }

    @Override
    public Object toObject() {
        return value;
    }

    @Override
    public void loadFromObject(Object o) {
        this.value = String.valueOf(o);
    }

    @Override
    public boolean canLoadFromItemStack(ItemStack itemStack) {
        return false;
    }

    @Override
    public void loadFromItemStack(ItemStack itemStack) {
        // EMPTY
    }

    @Override
    public boolean compareWithItemStack(ItemStack itemStack, UUID uuid, Map<String, StringReplacer> map) {
        return false;
    }
}
