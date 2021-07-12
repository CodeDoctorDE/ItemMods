package com.github.codedoctorde.itemmods.pack.template.block;

import com.github.codedoctorde.itemmods.api.block.CustomBlock;
import com.github.codedoctorde.itemmods.pack.PackBlock;
import com.github.codedoctorde.itemmods.pack.template.CustomTemplate;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author CodeDoctorDE
 */
public interface CustomBlockTemplate extends CustomTemplate<PackBlock, CustomBlock> {
    void loadConfig(JsonElement data, PackBlock block);

    @NotNull JsonElement saveConfig(PackBlock block);

    /**
     * Runs every tick when block is loaded.
     */
    void tick(CustomBlock customBlock);
}
