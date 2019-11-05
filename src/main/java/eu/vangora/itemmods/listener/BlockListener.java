package eu.vangora.itemmods.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

public class BlockListener implements Listener {
    @EventHandler
    public void onBlockFall(EntityBlockFormEvent event) {
        if (event.getEntity().getType() == EntityType.FALLING_BLOCK) {

        }
    }
}
