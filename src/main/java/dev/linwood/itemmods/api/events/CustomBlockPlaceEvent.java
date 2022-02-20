package dev.linwood.itemmods.api.events;

import dev.linwood.itemmods.pack.PackObject;
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
    private final @NotNull
    PackObject packObject;
    private final @NotNull
    Location location;
    private final @Nullable
    Player player;
    private boolean isCancelled;

    public CustomBlockPlaceEvent(@NotNull Location location, @NotNull PackObject packObject, @Nullable Player player) {
        this.packObject = packObject;
        this.location = location;
        this.player = player;
        this.isCancelled = false;
    }

    public static @NotNull
    HandlerList getHandlerList() {
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
    public PackObject getPackObject() {
        return packObject;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }
}
