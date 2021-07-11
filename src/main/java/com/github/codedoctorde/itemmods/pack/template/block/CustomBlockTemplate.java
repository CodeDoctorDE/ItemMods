package com.github.codedoctorde.itemmods.pack.template.block;

import com.github.codedoctorde.itemmods.api.block.CustomBlock;
import com.github.codedoctorde.itemmods.pack.StaticBlock;
import com.github.codedoctorde.itemmods.pack.template.CustomTemplate;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author CodeDoctorDE
 */
public interface CustomBlockTemplate extends CustomTemplate<StaticBlock, CustomBlock> {
    void loadConfig(JsonElement data, StaticBlock block);

    @NotNull JsonElement saveConfig(StaticBlock block);

    /**
     * Runs every tick when block is loaded.
     */
    void tick(CustomBlock customBlock);
}
