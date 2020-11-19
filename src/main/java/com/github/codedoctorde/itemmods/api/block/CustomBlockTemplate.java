package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.api.CustomTemplate;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.config.CustomConfig;
import com.google.gson.JsonElement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author CodeDoctorDE
 */
public abstract class CustomBlockTemplate extends CustomTemplate<BlockConfig, CustomBlock> {
    public CustomBlockTemplate(String name) {
        super(name);
    }

    public abstract void loadConfig(JsonElement data, BlockConfig config);

    @NotNull
    public abstract JsonElement saveConfig(BlockConfig config);

    /**
     * Runs every tick when block is loaded.
     */
    public abstract void tick(CustomBlock customBlock);
}
