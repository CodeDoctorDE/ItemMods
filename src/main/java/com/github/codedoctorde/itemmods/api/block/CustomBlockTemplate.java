package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.google.gson.JsonElement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public interface CustomBlockTemplate {

    ItemStack getIcon();

    /**
     * If the server loads a chunk with this block
     *
     * @param customBlock
     */
    void load(CustomBlock customBlock);

    /**
     * If the server unloads a chunk with this block
     *
     * @param customBlock
     */
    void unload(CustomBlock customBlock);

    void loadConfig(JsonElement data, BlockConfig config);

    boolean openConfigGui(BlockConfig blockConfig, Player player);

    JsonElement saveConfig(BlockConfig config);

    String getName();

    /**
     * Runs every tick when block is loaded.
     */
    void tick(CustomBlock customBlock);
}
