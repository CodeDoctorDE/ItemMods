package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import com.gitlab.codedoctorde.api.server.Version;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemConfig {
    private String name;
    private String tag = "";
    private String displayName;
    private ItemStack itemStack;
    private boolean canRename = true;
    private int pickaxe = 0;
    private int shovel = 0;
    private int axe = 0;
    private int hoe = 0;
    private int damage = 0;
    private int speed = 0;
    private final List<String> onWear = new ArrayList<>();
    private final List<String> onOffHand = new ArrayList<>();
    private final List<String> onMainHand = new ArrayList<>();
    private final List<String> onDrop = new ArrayList<>();
    private final List<String> onPickup = new ArrayList<>();
    private final List<String> onRightClick = new ArrayList<>();
    @Nullable
    private String templateName = null;
    private JsonObject templateConfig = new JsonObject();
    private final NamespacedKey typeNamespace = new NamespacedKey(ItemMods.getPlugin(), "type");


    public ItemConfig(String name) {
        this.displayName = name;
        this.name = name;
        this.tag = "itemmods:" + name.replaceAll("\\s+", "");
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
        ItemMeta itemMeta = other.getItemMeta();
        assert itemMeta != null;
        if (Version.getVersion().isLowerThan(Version.v1_15))
            return Objects.equals(itemMeta.getCustomTagContainer().getCustomTag(typeNamespace, ItemTagType.STRING), tag);
        else
            return itemMeta.getPersistentDataContainer().getOrDefault(typeNamespace, PersistentDataType.STRING, "").equals(tag);
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

    public int getAxe() {
        return axe;
    }

    public void setAxe(int axe) {
        this.axe = axe;
    }

    public int getHoe() {
        return hoe;
    }

    public void setHoe(int hoe) {
        this.hoe = hoe;
    }

    public int getPickaxe() {
        return pickaxe;
    }

    public void setPickaxe(int pickaxe) {
        this.pickaxe = pickaxe;
    }

    public int getShovel() {
        return shovel;
    }

    public void setShovel(int shovel) {
        this.shovel = shovel;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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
        ItemStack give = itemStack.clone();
        ItemMeta itemMeta = give.getItemMeta();
        assert itemMeta != null;
        if (Version.getVersion().isLowerThan(Version.v1_15))
            itemMeta.getCustomTagContainer().setCustomTag(typeNamespace, ItemTagType.STRING, tag);
        else
            itemMeta.getPersistentDataContainer().set(typeNamespace, PersistentDataType.STRING, tag);
        give.setItemMeta(itemMeta);
        return give;
    }
}
