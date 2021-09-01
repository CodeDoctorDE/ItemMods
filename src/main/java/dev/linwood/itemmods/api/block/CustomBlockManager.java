package dev.linwood.itemmods.api.block;

import dev.linwood.itemmods.api.events.CustomBlockPlaceEvent;
import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CustomBlockManager {

    public static @NotNull String locationToString(final @Nullable Location location) {
        if (location == null) return "";
        return Objects.requireNonNull(location.getWorld()).getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }

    public static @NotNull Location stringToLocation(final @NotNull String location) {
        String[] locationArray = location.split(":");
        return new Location(Bukkit.getWorld(locationArray[0]), Double.parseDouble(locationArray[1]),
                Double.parseDouble(locationArray[2]),
                Double.parseDouble(locationArray[3]));
    }

    /**
     * @param location   The location where the custom block will be placed!
     * @param packObject The block config for the custom block
     * @param player     The player who is placing the block
     * @return Returns if it was placed!
     */
    public CustomBlock setCustomBlock(Location location, PackObject packObject, @Nullable Player player) {
        var customBlock = new CustomBlock(location);
        if (customBlock.getConfig() != null)
            return null;
        if (location.getBlock().getType().isSolid())
            return null;
        var event = new CustomBlockPlaceEvent(location, packObject, player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return null;
        Block block = Objects.requireNonNull(location.getWorld()).getBlockAt(location);
        block.setType(Material.SPAWNER);
        // TODO: CRAFT BUKKIT

        return customBlock;
    }
}
