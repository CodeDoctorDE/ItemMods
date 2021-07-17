package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.events.CustomBlockBreakEvent;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.BlockAsset;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class CustomBlock {
    private static final NamespacedKey TYPE_KEY = new NamespacedKey(ItemMods.getPlugin(), "customblock_type");
    private static final NamespacedKey DATA_KEY = new NamespacedKey(ItemMods.getPlugin(), "customblock_data");
    private final Location location;

    public CustomBlock(Location location) {
        this.location = location;
    }

    public CustomBlock(Block block) {
        this(block.getLocation());
    }

    public @Nullable BlockAsset getConfig() {
        var type = getType();
        if (type == null)
            return null;
        var packObject = PackObject.fromIdentifier(type);
        if (packObject == null)
            return null;
        return packObject.getBlock();
    }

    public Location getLocation() {
        return location;
    }

    public @Nullable String getType() {
        return getString(TYPE_KEY);
    }

    public @Nullable String getData() {
        return getString(DATA_KEY);
    }

    public void setData(String data) {
        setString(DATA_KEY, data);
    }


    public boolean breakBlock(Player player) {
        //BlockConfig config = getConfig();
        /*if (config == null || dropType == null) return false;
        if (config.getBlock() != null)
            getBlock().setType(Material.AIR);
        getBlock().getDrops().clear();
        List<ItemStack> drops = new ArrayList<>();
        if (dropType == BlockDropType.SILK_TOUCH && config.getReferenceItemAsset() != null)
            drops.add(config.getReferenceItemAsset().giveItemStack());
        else if (dropType == BlockDropType.DROP || config.getReferenceItemAsset() == null)
            getConfig().getDrops().stream().filter(drop -> new Random().nextInt(99) + 1 <= drop.getRarity()).forEach(drop -> drops.add(drop.getItemStack()));
        else if (dropType == BlockDropType.FORTUNE)
            getConfig().getFortuneDrops().stream().filter(drop -> new Random().nextInt(99) + 1 <= drop.getRarity()).forEach(drop -> drops.add(drop.getItemStack()));*/
        CustomBlockBreakEvent event = new CustomBlockBreakEvent(this, new ArrayList<>(), player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return false;
        Location dropLocation = getBlock().getLocation().clone().add(0.5, 0, 0.5);
        event.getDrops().forEach(drop -> Objects.requireNonNull(dropLocation.getWorld()).dropItemNaturally(dropLocation, drop));
        return true;
    }

    public Block getBlock() {
        return location.getBlock();
    }

    private @Nullable String getString(NamespacedKey key) {
        BlockState blockState = getBlock().getState();
        if (blockState instanceof TileState) {
            TileState tileState = (TileState) blockState;
            return tileState.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }
        return null;
    }

    public void setString(NamespacedKey key, String value) {
        BlockState blockState = getBlock().getState();
        if (blockState instanceof TileState) {
            TileState tileState = (TileState) blockState;
            tileState.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
            tileState.update();
        }
    }

    /**
     * Configure the PersistentTagContainer to the default values
     */
    public void configure() {
        setString(new NamespacedKey(ItemMods.getPlugin(), "data"), "");
    }
}
