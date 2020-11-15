package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplateData;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemConfig extends CustomConfig {
    private ItemStack itemStack;
    private boolean canRename = true;
    @NotNull
    private CustomItemTemplateData template = new CustomItemTemplateData();
    private final List<CustomItemTemplateData> modifiers = new ArrayList<>();

    public ItemConfig(String namespace, String name) {
        super(namespace, name);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean isSimilar(@NotNull ItemStack other) {
        ItemMeta itemMeta = other.getItemMeta();
        if (itemMeta == null)
            return false;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        String otherIdentifier = container.get(new NamespacedKey(ItemMods.getPlugin(), "type"), PersistentDataType.STRING);
        return Objects.equals(otherIdentifier, getIdentifier());
    }

    public boolean isCanRename() {
        return canRename;
    }

    public void setCanRename(boolean canRename) {
        this.canRename = canRename;
    }

    public @NotNull CustomItemTemplateData getTemplate() {
        return template;
    }

    public void setTemplate(@NotNull CustomItemTemplateData template) {
        this.template = template;
    }

    public List<CustomItemTemplateData> getModifiers() {
        return modifiers;
    }

    public ItemStack giveItemStack() {
        if (itemStack == null)
            return null;
        ItemStack give = itemStack.clone();
        ItemMeta itemMeta = give.getItemMeta();
        assert itemMeta != null;
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(ItemMods.getPlugin(), "type"), PersistentDataType.STRING, getIdentifier());
        give.setItemMeta(itemMeta);
        return give;
    }
}
