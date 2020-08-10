package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.api.ui.Gui;
import com.google.gson.JsonElement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public interface CustomBlockTemplate {

    @NotNull
    ItemStack getIcon(BlockConfig blockConfig);

    @NotNull
    ItemStack getMainIcon(BlockConfig blockConfig);

    /**
     * If the server loads a chunk with this block
     *
     * @param customBlock The custom block which is loaded
     */
    void load(CustomBlock customBlock);

    /**
     * If the server unloads a chunk with this block
     *
     * @param customBlock The custom block which is unloaded
     */
    void unload(CustomBlock customBlock);

    void loadConfig(JsonElement data, BlockConfig config);

    boolean openConfigGui(BlockConfig blockConfig, Player player);

    @NotNull
    JsonElement saveConfig(BlockConfig config);

    @NotNull
    String getName();

    /**
     * Runs every tick when block is loaded.
     */
    void tick(CustomBlock customBlock);
}
