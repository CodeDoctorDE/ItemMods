package com.github.codedoctorde.itemmods.utils;

import com.github.codedoctorde.itemmods.ItemMods;
import org.bstats.bukkit.Metrics;

public class PluginMetrics {
    public static Metrics metrics = new Metrics(ItemMods.getPlugin(), 5996);
    public static void runMetrics() {
        metrics.getPluginData();
    }
}
