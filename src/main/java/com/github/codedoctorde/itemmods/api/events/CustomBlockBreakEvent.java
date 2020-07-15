package com.github.codedoctorde.itemmods.api.events;

import com.github.codedoctorde.itemmods.api.block.CustomBlock;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author CodeDoctorDE
 */
public class CustomBlockBreakEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final CustomBlock customBlock;
    private List<ItemStack> drops;
    private CustomBlock.BlockDropType dropType;
    private boolean isCancelled;

    public CustomBlockBreakEvent(CustomBlock customBlock, List<ItemStack> drops, CustomBlock.BlockDropType dropType) {
        this.customBlock = customBlock;
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

    public CustomBlock.BlockDropType getDropType() {
        return dropType;
    }

    public CustomBlock getCustomBlock() {
        return customBlock;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }
}
