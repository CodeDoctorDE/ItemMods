package com.github.codedoctorde.itemmods.api.block;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.events.CustomBlockBreakEvent;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CustomBlock {
    private final Location location;

    public CustomBlock(Location location) {
        this.location = location;
    }
    public CustomBlock(Block block){
        this(block.getLocation());
    }

    public BlockConfig getConfig() {
        return ItemMods.getPlugin().getMainConfig().getBlock(getIdentifier());
    }

    public Location getLocation() {
        return location;
    }

    @Nullable
    public ArmorStand getArmorStand() {
        Location entityLocation = location.clone().add(0.5, 0, 0.5);
        List<Entity> entities = new ArrayList<>(Objects.requireNonNull(entityLocation.getWorld()).getNearbyEntities(entityLocation, 0.05, 0.001, 0.05));
        return (ArmorStand) entities.stream().filter(entity -> entity instanceof ArmorStand && entity.getLocation().getY() == location.getY()).findFirst().orElse(null);
    }

    public String getIdentifier(){
        return getString(new NamespacedKey(ItemMods.getPlugin(), "type"));
    }
    public void setIdentifier(String identifier){
        setString(new NamespacedKey(ItemMods.getPlugin(), "type"), identifier);
    }

    public boolean breakBlock(Player player){
        if (player.getGameMode() == GameMode.CREATIVE)
            return breakBlock(CustomBlock.BlockDropType.NOTHING, player);
        else if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))
            return breakBlock(CustomBlock.BlockDropType.SILK_TOUCH, player);
        else if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.LUCK))
            return breakBlock(CustomBlock.BlockDropType.FORTUNE, player);
        else
            return breakBlock(CustomBlock.BlockDropType.DROP, player);
    }

    public boolean breakBlock(BlockDropType dropType, Player player) {
        BlockConfig config = getConfig();
        if (config == null || dropType == null) return false;
        if (config.getBlock() != null)
            getBlock().setType(Material.AIR);
        getBlock().getDrops().clear();
        List<ItemStack> drops = new ArrayList<>();
        if (dropType == BlockDropType.SILK_TOUCH && config.getReferenceItemConfig() != null)
            drops.add(config.getReferenceItemConfig().giveItemStack());
        else if (dropType == BlockDropType.DROP || config.getReferenceItemConfig() == null)
            getConfig().getDrops().stream().filter(drop -> new Random().nextInt(99) + 1 <= drop.getRarity()).forEach(drop -> drops.add(drop.getItemStack()));
        else if (dropType == BlockDropType.FORTUNE)
            getConfig().getFortuneDrops().stream().filter(drop -> new Random().nextInt(99) + 1 <= drop.getRarity()).forEach(drop -> drops.add(drop.getItemStack()));
        CustomBlockBreakEvent event = new CustomBlockBreakEvent(this, drops, dropType, player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return false;
        Location dropLocation = getBlock().getLocation().clone().add(0.5, 0, 0.5);
        event.getDrops().forEach(drop -> Objects.requireNonNull(dropLocation.getWorld()).dropItemNaturally(dropLocation, drop));
        if (getArmorStand() != null)
            getArmorStand().remove();
        return true;
    }

    public Block getBlock() {
        return location.getBlock();
    }

    public String getString(NamespacedKey key) {
        BlockState blockState = getBlock().getState();
        if (blockState instanceof TileState) {
            TileState tileState = (TileState) blockState;
            return tileState.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }
        ArmorStand armorStand = getArmorStand();
        if (armorStand != null) return armorStand.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return null;
    }

    public void setString(NamespacedKey key, String value) {
        BlockState blockState = getBlock().getState();
        if (blockState instanceof TileState) {
            TileState tileState = (TileState) blockState;
            tileState.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
            tileState.update();
        }
        ArmorStand armorStand = getArmorStand();
        if (armorStand != null) {
            armorStand.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        }
    }

    /**
     * Configure the PersistentTagContainer to the default values
     */
    public void configure() {
        setString(new NamespacedKey(ItemMods.getPlugin(), "data"), "");
    }

    public enum BlockDropType {
        SILK_TOUCH, DROP, NOTHING, FORTUNE
    }
}
