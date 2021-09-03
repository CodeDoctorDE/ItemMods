package dev.linwood.itemmods.api.block;

import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomBlock {
    protected static final NamespacedKey TYPE_KEY = new NamespacedKey(ItemMods.getPlugin(), "custom_block_type");
    protected static final NamespacedKey DATA_KEY = new NamespacedKey(ItemMods.getPlugin(), "custom_block_data");
    private final Location location;

    public CustomBlock(Location location) {
        this.location = location;
    }

    public CustomBlock(@NotNull Block block) {
        this(block.getLocation());
    }

    public @Nullable BlockAsset getConfig() {
        try {
            var type = getType();
            if (type == null)
                return null;
            var packObject = new PackObject(type);
            return packObject.getBlock();
        } catch (Exception ignored) {
            return null;
        }
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

    public void setData(@NotNull String data) {
        setString(DATA_KEY, data);
    }


    public void breakBlock(Player player) {
        getBlock().breakNaturally(player.getInventory().getItemInMainHand());
    }

    public void breakBlock() {
        getBlock().breakNaturally();
    }

    public @NotNull Block getBlock() {
        return location.getBlock();
    }

    private @Nullable String getString(@NotNull NamespacedKey key) {
        BlockState blockState = getBlock().getState();
        if (blockState instanceof TileState) {
            TileState tileState = (TileState) blockState;
            return tileState.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }
        return null;
    }

    public void setString(@NotNull NamespacedKey key, @NotNull String value) {
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
