package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.api.CustomTemplate;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author CodeDoctorDE
 */
public interface CustomBlockTemplate extends CustomTemplate<BlockConfig, CustomBlock> {
    void loadConfig(JsonElement data, BlockConfig config);

    @NotNull JsonElement saveConfig(BlockConfig config);

    /**
     * Runs every tick when block is loaded.
     */
    void tick(CustomBlock customBlock);
}
