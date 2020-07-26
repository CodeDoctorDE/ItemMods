package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.api.block.CustomBlockTemplate;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import com.gitlab.codedoctorde.api.ui.Gui;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author CodeDoctorDE
 */
public abstract class ItemModsAddon {
    protected Set<CustomBlockTemplate> blockTemplates = new HashSet<>();
    protected Set<CustomItemTemplate> itemTemplates = new HashSet<>();

    @NotNull
    public abstract String getName();

    @NotNull
    public abstract ItemStack getIcon();

    @Nullable
    public abstract boolean openConfigGui();

    public CustomBlockTemplate[] getBlockTemplates() {
        return blockTemplates.toArray(new CustomBlockTemplate[0]);
    }

    public CustomItemTemplate[] getItemTemplates() {
        return itemTemplates.toArray(new CustomItemTemplate[0]);
    }

    protected void registerBlockTemplate(CustomBlockTemplate template) {
        blockTemplates.add(template);
    }

    protected void unregisterBlockTemplate(CustomBlockTemplate template) {
        blockTemplates.remove(template);
    }

    protected void registerItemTemplate(CustomItemTemplate template) {
        itemTemplates.add(template);
    }

    protected void unregisterItemTemplate(CustomItemTemplate template) {
        itemTemplates.remove(template);
    }
}
