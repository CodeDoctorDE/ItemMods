package com.github.codedoctorde.itemmods.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public class CustomBlockPlaceEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final BlockConfig blockConfig;
    private final Location location;
    private final Player player;
    private boolean isCancelled;

    public CustomBlockPlaceEvent(Location location, BlockConfig blockConfig, Player player) {
        this.blockConfig = blockConfig;
        this.location = location;
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
    public Location getLocation() {
        return location;
    }

    @NotNull
    public BlockConfig getBlockConfig() {
        return blockConfig;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }
}
