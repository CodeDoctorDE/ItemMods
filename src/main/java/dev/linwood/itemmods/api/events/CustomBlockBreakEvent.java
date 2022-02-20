package dev.linwood.itemmods.api.events;

import dev.linwood.itemmods.api.block.CustomBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public class CustomBlockBreakEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final CustomBlock customBlock;
    private final Player player;
    private boolean isCancelled;

    public CustomBlockBreakEvent(CustomBlock customBlock, Player player) {
        this.customBlock = customBlock;
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
    public CustomBlock getCustomBlock() {
        return customBlock;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }
}
