package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemConfig {
    private String name;
    private String displayName;
    private ItemStack itemStack;
    private boolean canRename = true;
    private Integer hardness = null;
    private final List<String> onWear = new ArrayList<>();
    private final List<String> onOffHand = new ArrayList<>();
    private final List<String> onMainHand = new ArrayList<>();
    private final List<String> onDrop = new ArrayList<>();
    private final List<String> onPickup = new ArrayList<>();
    private final List<String> onRightClick = new ArrayList<>();
    @Nullable
    private String templateName = null;
    private JsonObject templateConfig = new JsonObject();
    private String tag;


    public ItemConfig(String name) {
        this.displayName = name;
        this.name = name;
        this.tag = "itemmods:" + name.replaceAll("\\s+", "");
    }

    NamespacedKey getTypeNamespace() {
        return new NamespacedKey(ItemMods.getPlugin(), "type");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isSimilar(ItemStack other) {
        if(other == null || other.getItemMeta() == null)
            return false;
        ItemMeta itemMeta = other.getItemMeta();
        if (itemMeta == null || !itemMeta.getPersistentDataContainer().has(getTypeNamespace(), PersistentDataType.STRING))
            return false;
        return itemMeta.getPersistentDataContainer().getOrDefault(getTypeNamespace(), PersistentDataType.STRING, "").equals(tag);
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
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


    @Nullable
    public CustomItemTemplate getTemplate() {
        if (templateName == null)
            return null;
        try {
            return ItemMods.getPlugin().getApi().getItemTemplate(templateName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        templateName = null;
        return null;
    }

    public void setTemplate(@Nullable CustomItemTemplate itemTemplate) {
        if (itemTemplate == null)
            templateName = null;
        else
            this.templateName = itemTemplate.getClass().getName();
        templateConfig = new JsonObject();
    }

    @Nullable
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(@Nullable String templateName) {
        this.templateName = templateName;
        templateConfig = new JsonObject();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag.replaceAll("\\s+", "");
    }

    @NotNull
    public JsonObject getTemplateConfig() {
        if (templateConfig == null)
            templateConfig = new JsonObject();
        return templateConfig;
    }

    public void setTemplateConfig(@NotNull JsonObject templateConfig) {
        this.templateConfig = templateConfig;
    }

    public ItemStack giveItemStack() {
        if(itemStack == null)
            return null;
        ItemStack give = itemStack.clone();
        ItemMeta itemMeta = give.getItemMeta();
        assert itemMeta != null;
        itemMeta.getPersistentDataContainer().set(getTypeNamespace(), PersistentDataType.STRING, tag);
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
