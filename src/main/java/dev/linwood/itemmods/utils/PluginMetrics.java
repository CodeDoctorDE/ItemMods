package dev.linwood.itemmods.utils;

import dev.linwood.itemmods.ItemMods;
import org.bstats.bukkit.Metrics;

public class PluginMetrics {
    public static Metrics metrics;

    public static void runMetrics() {
        try {
            metrics = new Metrics(ItemMods.getPlugin(), 5996);
            metrics.getPluginData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
