package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.api.block.StaticCustomBlock;
import com.github.codedoctorde.itemmods.api.item.StaticCustomItem;
import com.github.codedoctorde.itemmods.pack.template.block.CustomBlockTemplate;
import com.github.codedoctorde.itemmods.pack.template.item.CustomItemTemplate;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author CodeDoctorDE
 */
public abstract class ItemModsAddon {
    protected final JavaPlugin plugin;
    private final String name;
    protected Set<CustomBlockTemplate> blockTemplates = new HashSet<>();
    protected Set<CustomBlockTemplate> blockModifiers = new HashSet<>();
    protected Set<CustomItemTemplate> itemTemplates = new HashSet<>();
    protected Set<CustomItemTemplate> itemModifiers = new HashSet<>();
    protected Set<StaticCustomBlock> staticCustomBlocks = new HashSet<>();
    protected Set<StaticCustomItem> staticCustomItems = new HashSet<>();

    public ItemModsAddon(final JavaPlugin plugin, final String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NotNull
    public abstract ItemStack getIcon();

    public abstract boolean openConfigGui();

    public CustomBlockTemplate[] getBlockTemplates() {
        return blockTemplates.toArray(new CustomBlockTemplate[0]);
    }

    public CustomItemTemplate[] getItemTemplates() {
        return itemTemplates.toArray(new CustomItemTemplate[0]);
    }

    public StaticCustomBlock[] getStaticCustomBlocks() {
        return staticCustomBlocks.toArray(new StaticCustomBlock[0]);
    }

    public StaticCustomItem[] getStaticCustomItems() {
        return staticCustomItems.toArray(new StaticCustomItem[0]);
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

    protected void registerBlockModifier(CustomBlockTemplate template) {
        blockModifiers.add(template);
    }

    protected void unregisterBlockModifier(CustomBlockTemplate template) {
        blockModifiers.remove(template);
    }

    protected void registerItemModifier(CustomItemTemplate template) {
        itemModifiers.add(template);
    }

    protected void unregisterItemModifier(CustomItemTemplate template) {
        itemModifiers.remove(template);
    }

    protected void registerStaticCustomBlock(StaticCustomBlock block) {
        staticCustomBlocks.add(block);
    }

    protected void unregisterStaticCustomBlock(StaticCustomBlock block) {
        staticCustomBlocks.remove(block);
    }

    protected void registerStaticCustomItem(StaticCustomItem item) {
        staticCustomItems.add(item);
    }

    protected void unregisterStaticCustomItem(StaticCustomItem item) {
        staticCustomItems.remove(item);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
