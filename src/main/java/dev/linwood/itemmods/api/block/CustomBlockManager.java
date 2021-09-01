package dev.linwood.itemmods.api.block;

import de.tr7zw.changeme.nbtapi.NBTTileEntity;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.api.events.CustomBlockPlaceEvent;
import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
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
    public CustomBlock create(Location location, PackObject packObject, @Nullable Player player) {
        var customBlock = new CustomBlock(location);
        if (customBlock.getConfig() != null)
            return null;
        var asset = packObject.getBlock();
        if (location.getBlock().getType().isSolid() || asset == null)
            return null;
        var model = asset.getModel();
        if(model == null || model.getFallbackTexture() == null)
            return null;
        var event = new CustomBlockPlaceEvent(location, packObject, player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return null;
        Block block = Objects.requireNonNull(location.getWorld()).getBlockAt(location);
        block.setType(Material.SPAWNER);
        ((TileState)block.getState()).getPersistentDataContainer().set(CustomBlock.TYPE_KEY, PersistentDataType.STRING, packObject.toString());
            NBTTileEntity tent = new NBTTileEntity(block.getState());
            tent.setInteger("RequiredPlayerRange", 0);
            var spawnData = tent.addCompound("SpawnData");
            spawnData.setString("id", EntityType.ARMOR_STAND.getKey().toString());
            var armorItems = spawnData.getCompoundList("ArmorItems");
            // Boots
            armorItems.addCompound();
            // Leggings
            armorItems.addCompound();
            // Chest plate
            armorItems.addCompound();
            // Head
            var head = armorItems.addCompound();
            head.setString("id", model.getFallbackTexture().getKey().toString());
            head.setDouble("Count", 1d);

        return customBlock;
    }
}
