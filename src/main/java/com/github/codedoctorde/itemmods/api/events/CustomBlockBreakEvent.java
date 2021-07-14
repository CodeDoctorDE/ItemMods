package com.github.codedoctorde.itemmods.api.events;

import com.github.codedoctorde.itemmods.api.block.CustomBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author CodeDoctorDE
 */
public class CustomBlockBreakEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final CustomBlock customBlock;
    private final Player player;
    private List<ItemStack> drops;
    private boolean isCancelled;

    public CustomBlockBreakEvent(CustomBlock customBlock, List<ItemStack> drops, Player player) {
        this.customBlock = customBlock;
        this.drops = drops;
        this.player = player;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @NotNull
    public CustomBlock getCustomBlock() {
        return customBlock;
    }

    @NotNull
    public List<ItemStack> getDrops() {
        return drops;
    }

    public void setDrops(@NotNull List<ItemStack> drops) {
        this.drops = drops;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }
}
