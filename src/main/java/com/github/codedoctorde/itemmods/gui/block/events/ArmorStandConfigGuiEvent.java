package com.github.codedoctorde.itemmods.gui.block.events;

import com.github.codedoctorde.itemmods.config.ArmorStandBlockConfig;
import org.bukkit.entity.Player;

public interface ArmorStandConfigGuiEvent {
    void onEvent(Player player, ArmorStandBlockConfig config);

    void onCancel(Player player);
}
