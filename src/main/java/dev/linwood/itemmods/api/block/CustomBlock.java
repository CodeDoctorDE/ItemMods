package dev.linwood.itemmods.api.block;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.api.CustomElement;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class CustomBlock implements CustomElement<BlockAsset> {
    protected static final NamespacedKey TYPE_KEY = new NamespacedKey(ItemMods.getPlugin(), "custom_block_type");
    protected static final NamespacedKey DATA_KEY = new NamespacedKey(ItemMods.getPlugin(), "custom_block_data");
    private final Location location;

    public CustomBlock(Location location) {
        this.location = location;
    }

    public CustomBlock(@NotNull Block block) {
        this(block.getLocation());
    }

    public @Nullable
    BlockAsset getConfig() {
        var packObject = getPackObject();
        if (packObject == null)
            return null;
        return packObject.getAsset(BlockAsset.class);
    }

    public Location getLocation() {
        return location;
    }

    private @NotNull
    String getType() {
        return getString(TYPE_KEY);
    }

    public @NotNull
    String getData() {
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

    public @NotNull
    Block getBlock() {
        return location.getBlock();
    }

    private @NotNull
    String getString(@NotNull NamespacedKey key) {
        BlockState blockState = getBlock().getState();
        if (blockState instanceof TileState) {
            TileState tileState = (TileState) blockState;
            return tileState.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
        }
        return "";
    }

    private void setString(@NotNull NamespacedKey key, @NotNull String value) {
        BlockState blockState = getBlock().getState();
        if (blockState instanceof TileState) {
            TileState tileState = (TileState) blockState;
            tileState.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
            tileState.update(true, false);
        }
    }

    /**
     * Configure the PersistentTagContainer to the default values
     */
    public void configure() {
        setString(new NamespacedKey(ItemMods.getPlugin(), "data"), "");
    }

    public @Nullable
    PackObject getPackObject() {
        var type = getType();
        if (type.equals(""))
            return null;
        return new PackObject(type);
    }

    public void send(Player player) throws IOException {
        if (!ItemMods.hasProtocolLib()) return;
        var asset = getConfig();
        var packObject = getPackObject();
        if (asset == null) return;
        if (packObject == null) return;
        // Generate uuid
        var uuid = UUID.randomUUID();
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        // Set to armor stand
        //noinspection deprecation
        packet.getEntityTypeModifier().write(0, EntityType.GLOW_ITEM_FRAME);
        // Set armor stand location
        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());
        // Add uuid
        packet.getUUIDs().write(0, uuid);
        // Add random entity id
        var entityId = (int) (Math.random() * Integer.MAX_VALUE);
        packet.getIntegers().write(0, entityId);
        try {
            assert ItemMods.getProtocolManager() != null;
            ItemMods.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // Set fake block
        Material material = Material.STONE;
        ModelAsset model = asset.getModel();
        if (model != null) {
            material = model.getFallbackTexture();
            if (!material.isBlock())
                material = Material.STONE;
        }
        int customModelData = 0;
        var modelObject = asset.getModelObject();
        if (modelObject != null) {
            Integer modelData = modelObject.getCustomModel();
            customModelData = modelData == null ? 0 : modelData;
        }
        ItemStack itemStack = new ItemStackBuilder(material).customModelData(customModelData).build();
        // Set item frame item
        packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        var serializer = WrappedDataWatcher.Registry.getItemStackSerializer(false);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.WrappedDataWatcherObject itemFrameItem = new WrappedDataWatcher.WrappedDataWatcherObject(8, serializer);
        watcher.setObject(itemFrameItem, itemStack);
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        packet.getIntegers().write(0, entityId);
        try {
            assert ItemMods.getProtocolManager() != null;
            ItemMods.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        player.sendBlockChange(location, material.createBlockData());
    }
}
