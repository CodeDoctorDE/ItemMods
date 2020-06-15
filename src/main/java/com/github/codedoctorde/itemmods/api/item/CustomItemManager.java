package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomItemManager {

    public CustomItemManager() {
    }

    public List<ItemConfig> getItems() {
        return Main.getPlugin().getMainConfig().getItems();
    }

    /**
     * Get the item config by the given item stack.
     *
     * @param itemStack The item stack which you want the item config
     * @return the ItemConfig from the item stack. If this value is null, the item isn't a custom item.
     */
    @NotNull
    public CustomItem getCustomItem(ItemStack itemStack) {
        return new CustomItem(itemStack);
    }
}
