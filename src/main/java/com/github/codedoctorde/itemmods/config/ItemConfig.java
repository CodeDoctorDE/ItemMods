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
    private final List<String> onWear = new ArrayList<>();
    private final List<String> onOffHand = new ArrayList<>();
    private final List<String> onMainHand = new ArrayList<>();
    private final List<String> onDrop = new ArrayList<>();
    private final List<String> onPickup = new ArrayList<>();
    private final List<String> onRightClick = new ArrayList<>();
    private String name;
    private String namespace;
    private String displayName;
    private ItemStack itemStack;
    private boolean canRename = true;
    private Integer hardness = null;
    @Nullable
    private CustomItemTemplateData template;
    private final List<CustomItemTemplateData> modifiers = new ArrayList<>();

    public ItemConfig(String namespace, String name) {
        super(namespace, name);
    }

    public String getNamespace() {
        return namespace;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        String otherNamespace = container.get(new NamespacedKey(ItemMods.getPlugin(), "namespace"), PersistentDataType.STRING);
        String otherName = container.get(new NamespacedKey(ItemMods.getPlugin(), "name"), PersistentDataType.STRING);
        return Objects.equals(otherName, name) && Objects.equals(otherNamespace, namespace);
    }

    public boolean isCanRename() {
        return canRename;
    }

    public void setCanRename(boolean canRename) {
        this.canRename = canRename;
    }

    public List<String> getOnDrop() {
        return onDrop;
    }

    public List<String> getOnWear() {
        return onWear;
    }

    public List<String> getOnMainHand() {
        return onMainHand;
    }

    public List<String> getOnOffHand() {
        return onOffHand;
    }

    public List<String> getOnPickup() {
        return onPickup;
    }

    public List<String> getOnRightClick() {
        return onRightClick;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public @Nullable CustomItemTemplateData getTemplate() {
        return template;
    }

    public void setTemplate(@Nullable CustomItemTemplateData template) {
        this.template = template;
    }

    public List<CustomItemTemplateData> getModifiers() {
        return modifiers;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
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

    public Integer getHardness() {
        return hardness;
    }

    public void setHardness(Integer hardness) {
        this.hardness = hardness;
    }
}
