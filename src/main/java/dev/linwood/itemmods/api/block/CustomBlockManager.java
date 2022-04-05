package dev.linwood.itemmods.api.block;

import de.tr7zw.changeme.nbtapi.NBTTileEntity;
import dev.linwood.itemmods.api.events.CustomBlockPlaceEvent;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CustomBlockManager {
    private static CustomBlockManager instance;

    private CustomBlockManager() {
    }

    public static CustomBlockManager getInstance() {
        return instance == null ? instance = new CustomBlockManager() : instance;
    }

    private static @NotNull
    String locationToString(final @Nullable Location location) {
        if (location == null) return "";
        return Objects.requireNonNull(location.getWorld()).getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }

    private static @NotNull
    Location stringToLocation(final @NotNull String location) {
        String[] locationArray = location.split(":");
        return new Location(Bukkit.getWorld(locationArray[0]), Double.parseDouble(locationArray[1]),
                Double.parseDouble(locationArray[2]),
                Double.parseDouble(locationArray[3]));
    }

    public BlockAsset getAssetByKey(String key) throws UnsupportedOperationException {
        var packObject = new PackObject(key);
        return packObject.getAsset(BlockAsset.class);
    }

    public CustomBlock fromLocation(Location location) {
        return new CustomBlock(location);
    }

    public CustomBlock fromBlock(Block block) {
        return new CustomBlock(block);
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
        var asset = packObject.getAsset(BlockAsset.class);
        if (location.getBlock().getType().isSolid() || asset == null)
            return null;
        var model = asset.getModel();
        if (model == null)
            return null;
        var event = new CustomBlockPlaceEvent(location, packObject, player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return null;
        Block block = Objects.requireNonNull(location.getWorld()).getBlockAt(location);
        block.setType(Material.SPAWNER);
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        NBTTileEntity tent = new NBTTileEntity(block.getState());
        tent.setInteger("RequiredPlayerRange", 0);
        tent.setInteger("SpawnCount", 0);
        var spawnData = tent.addCompound("SpawnData");
        spawnData.setString("id", EntityType.ARMOR_STAND.getKey().toString());
        var handItems = spawnData.getCompoundList("HandItems");
        var hand = handItems.addCompound();
        hand.setString("id", model.getFallbackTexture().getKey().toString());
        hand.addCompound("tag").setInteger("CustomModelData", asset.getModelObject() == null ? null : asset.getModelObject().getCustomModel());
        hand.setByte("Count", (byte) 1);
        var state = (CreatureSpawner) block.getState();
        state.getPersistentDataContainer().set(CustomBlock.TYPE_KEY, PersistentDataType.STRING, packObject.toString());
        state.update();


        return customBlock;
    }
}
