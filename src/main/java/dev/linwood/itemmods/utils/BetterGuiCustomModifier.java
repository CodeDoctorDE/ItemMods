package dev.linwood.itemmods.utils;

import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import me.hsgamer.bettergui.lib.core.bukkit.item.ItemModifier;
import me.hsgamer.bettergui.lib.core.common.interfaces.StringReplacer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * @author CodeDoctorDE
 */
public class BetterGuiCustomModifier implements ItemModifier {
    private String value = "";

    @Override
    public @NotNull String getName() {
        return "itemmods";
    }

    @Override
    public @Nullable ItemStack modify(ItemStack itemStack, UUID uuid, @NotNull Map<String, StringReplacer> map) {
        var customItem = ItemMods.getCustomItemManager().create(new PackObject(StringReplacer.replace(value, uuid, map.values())));
        if (customItem == null)
            return null;
        return customItem.getItemStack();
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
