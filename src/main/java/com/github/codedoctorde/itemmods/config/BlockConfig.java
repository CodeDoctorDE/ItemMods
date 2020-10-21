package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplate;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplateData;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockConfig {
    private final List<DropConfig> drops = new ArrayList<>();
    private String name;
    private String tag;
    private String displayName;
    private BlockData block;
    private boolean drop = true;
    private boolean moving = false;
    private ArmorStandBlockConfig armorStand = null;
    private CustomBlockTemplateData templateData;
    private String referenceItem;
    private BlockDirectionType blockDirectionType = BlockDirectionType.NO;

    public BlockConfig(String name) {
        this.name = name;
        this.displayName = name;
        this.tag = "itemmods:" + name.replaceAll("\\s+", "");
    }

    public boolean checkBlock(BlockState block) {
        return armorStand != null || block instanceof TileState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomBlockTemplateData getTemplateData() {
        return templateData;
    }

    public void setTemplateData(CustomBlockTemplateData templateData) {
        this.templateData = templateData;
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
    @Deprecated(since = "1.5")
    public CustomBlockTemplate getTemplate() {
        return templateData.getTemplate();
    }

    @Deprecated(since = "1.5")
    public void setTemplate(@Nullable CustomBlockTemplate blockTemplate) {
        templateData.setTemplate(blockTemplate);
    }

    @Deprecated(since = "1.5")
    public String getTemplateName() {
        return templateData.getName();
    }

    @Deprecated(since = "1.5")
    public void setTemplateName(String templateName) {
        templateData.setName(templateName);
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

    public void setDrop(boolean drop) {
        this.drop = drop;
    }

    public List<DropConfig> getDrops() {
        return drops;
    }

    public List<DropConfig> getFortuneDrops() {
        return drops.stream().filter(DropConfig::isFortune).collect(Collectors.toList());
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

    @Deprecated(since = "1.5")
    public String getData() {
        return templateData.getData();
    }

    @Deprecated(since = "1.5")
    public void setData(String data) {
        templateData.setData(data);
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

    public BlockDirectionType getBlockDirectionType() {
        return blockDirectionType;
    }

    public void setBlockDirectionType(BlockDirectionType blockDirectionType) {
        this.blockDirectionType = blockDirectionType;
    }

    public enum CorrectResult {
        YES,
        ENTITY,
        NO_BLOCK
    }
}
