package com.github.codedoctorde.itemmods.api;

import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.google.gson.JsonElement;
import org.bukkit.inventory.ItemStack;

/**
 * @author CodeDoctorDE
 */
public interface CustomBlockTemplate {

    ItemStack getIcon();

    void load(JsonElement data, CustomBlock blockConfig);

    JsonElement save(CustomBlock blockConfig);

    void loadConfig(JsonElement data, BlockConfig config);

    Gui openConfig(BlockConfig blockConfig);

    JsonElement saveConfig(BlockConfig config);

    String getName();
}
