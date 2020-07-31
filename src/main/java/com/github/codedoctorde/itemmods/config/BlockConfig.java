package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplate;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockConfig {
    private String name;
    private String tag;
    private String displayName;
    private BlockData block;
    private boolean drop = true;
    private boolean moving = false;
    private String data = null;
    private ArmorStandBlockConfig armorStand = null;
    private String templateName;
    private final List<DropConfig> drops = new ArrayList<>();
    private String referenceItem;
    private int shovelHardness = 0;
    private int pickaxeHardness = 0;
    private int axeHardness = 0;
    private int hoeHardness = 0;
    private int swordHardness = 0;
    private int shearHardness = 0;
    private BlockDirectionType blockDirectionType = BlockDirectionType.NO;

    public boolean checkBlock(BlockState block) {
        return armorStand != null || block instanceof TileState;
    }

    BlockConfig() {
    }

    public BlockConfig(String name) {
        this.name = name;
        this.displayName = name;
        this.tag = "itemmods:" + name.replaceAll("\\s+", "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public BlockData getBlock() {
        return block;
    }

    public void setBlock(BlockData block) {
        this.block = block;
    }

    public boolean isArmorStand() {
        return armorStand != null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag.replaceAll("\\s+", "");
    }

    @Nullable
    public CustomBlockTemplate getTemplate() {
        if (templateName == null)
            return null;
        try {
            return ItemMods.getPlugin().getApi().getBlockTemplate(templateName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        templateName = null;
        return null;
    }

    public void setTemplate(@Nullable CustomBlockTemplate blockTemplate) {
        if(blockTemplate == null)
            templateName = null;
        else
            templateName = blockTemplate.getClass().getName();
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Nullable
    public ArmorStandBlockConfig getArmorStand() {
        return armorStand;
    }

    public void setArmorStand(ArmorStandBlockConfig armorStand) {
        this.armorStand = armorStand;
    }

    public boolean isDrop() {
        return drop;
    }

    public List<DropConfig> getDrops() {
        return drops;
    }

    public List<DropConfig> getFortuneDrops() {
        return drops.stream().filter(DropConfig::isFortune).collect(Collectors.toList());
    }

    public void setDrop(boolean drop) {
        this.drop = drop;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public CorrectResult correct() {
        if (block == null && armorStand == null)
            return CorrectResult.NO_BLOCK;
        return CorrectResult.YES;
    }

    public enum CorrectResult {
        YES,
        ENTITY,
        NO_BLOCK
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Nullable
    public String getReferenceItem() {
        return referenceItem;
    }

    public void setReferenceItem(@Nullable String referenceItem) {
        this.referenceItem = referenceItem;
    }

    @Nullable
    public ItemConfig getReferenceItemConfig() {
        if (referenceItem == null)
            return null;
        else
            return ItemMods.getPlugin().getMainConfig().getItem(referenceItem);
    }

    public void setReferenceItemConfig(@Nullable ItemConfig itemConfig) {
        if (itemConfig == null)
            this.referenceItem = null;
        else
            this.referenceItem = itemConfig.getTag();
    }

    public int getAxeHardness() {
        return axeHardness;
    }

    public void setAxeHardness(int axeHardness) {
        this.axeHardness = axeHardness;
    }

    public int getHoeHardness() {
        return hoeHardness;
    }

    public void setHoeHardness(int hoeHardness) {
        this.hoeHardness = hoeHardness;
    }

    public int getPickaxeHardness() {
        return pickaxeHardness;
    }

    public void setPickaxeHardness(int pickaxeHardness) {
        this.pickaxeHardness = pickaxeHardness;
    }

    public int getShearHardness() {
        return shearHardness;
    }

    public void setShearHardness(int shearHardness) {
        this.shearHardness = shearHardness;
    }

    public int getShovelHardness() {
        return shovelHardness;
    }

    public void setShovelHardness(int shovelHardness) {
        this.shovelHardness = shovelHardness;
    }

    public int getSwordHardness() {
        return swordHardness;
    }

    public void setSwordHardness(int swordHardness) {
        this.swordHardness = swordHardness;
    }

    public BlockDirectionType getBlockDirectionType() {
        return blockDirectionType;
    }

    public void setBlockDirectionType(BlockDirectionType blockDirectionType) {
        this.blockDirectionType = blockDirectionType;
    }
}
