package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplateData;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockConfig extends CustomConfig<CustomBlockTemplateData> {
    private final List<DropConfig> drops = new ArrayList<>();
    private BlockData block;
    private boolean drop = true;
    private boolean moving = false;
    private String nbt;
    private ArmorStandBlockConfig armorStand = null;
    private String referenceItem;
    private BlockDirectionType blockDirectionType = BlockDirectionType.NO;

    public BlockConfig(String namespace, String name) {
        super(namespace, name);
    }

    public boolean checkBlock(BlockState block) {
        return armorStand != null || block instanceof TileState;
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

    @Nullable
    public String getReferenceItem() {
        return referenceItem;
    }

    public void setReferenceItem(@Nullable String referenceItem) {
        this.referenceItem = referenceItem;
    }

    @Nullable
    public ItemConfig getReferenceItemConfig() {
        return referenceItem == null ? null : ItemMods.getMainConfig().getItem(referenceItem);
    }

    public void setReferenceItemConfig(@Nullable ItemConfig itemConfig) {
        if (itemConfig == null)
            this.referenceItem = null;
        else
            this.referenceItem = itemConfig.getIdentifier();
    }

    public BlockDirectionType getBlockDirectionType() {
        return blockDirectionType;
    }

    public void setBlockDirectionType(BlockDirectionType blockDirectionType) {
        this.blockDirectionType = blockDirectionType;
    }

    public String getNbt() {
        return nbt;
    }

    public void setNbt(String nbt) {
        this.nbt = nbt;
    }

    public enum CorrectResult {
        YES,
        ENTITY,
        NO_BLOCK
    }
}
