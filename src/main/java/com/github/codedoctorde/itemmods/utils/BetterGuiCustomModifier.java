package com.github.codedoctorde.itemmods.utils;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.PackObject;
import me.hsgamer.bettergui.lib.core.bukkit.item.ItemModifier;
import me.hsgamer.bettergui.lib.core.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author CodeDoctorDE
 */
public class BetterGuiCustomModifier implements ItemModifier {
    private String value = "";

    @Override
    public @NotNull String getName() {
        return "customitem";
    }

    @Override
    public @Nullable ItemStack modify(ItemStack itemStack, UUID uuid, @NotNull Map<String, StringReplacer> map) {
        return ItemMods.getCustomItemManager().create(Objects.requireNonNull(PackObject.fromIdentifier(StringReplacer.replace(value, uuid, map.values()))));
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
