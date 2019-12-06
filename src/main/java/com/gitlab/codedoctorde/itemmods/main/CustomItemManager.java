package com.gitlab.codedoctorde.itemmods.main;

import com.gitlab.codedoctorde.itemmods.config.ItemConfig;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomItemManager {
    private List<ItemConfig> itemConfigs;

    CustomItemManager(List<ItemConfig> itemConfigs) {
        this.itemConfigs = itemConfigs;
    }

    /**
     * Get the item config by the given item stack.
     *
     * @param itemStack The item stack which you want the item config
     * @return the ItemConfig from the item stack
     */
    @Nullable
    public ItemConfig getItemConfig(ItemStack itemStack) {
        return itemConfigs.stream().filter(itemConfig -> itemConfig.getItemStack().isSimilar(itemStack)).findFirst().orElse(null);
    }


    public List<ItemConfig> getItemConfigs() {
        return itemConfigs;
    }
}
