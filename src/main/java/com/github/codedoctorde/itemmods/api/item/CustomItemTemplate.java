package com.github.codedoctorde.itemmods.api.item;

import com.github.codedoctorde.itemmods.api.CustomTemplate;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author CodeDoctorDE
 */
public abstract class CustomItemTemplate extends CustomTemplate<ItemConfig> {
    public CustomItemTemplate(String name) {
        super(name);
    }
}
