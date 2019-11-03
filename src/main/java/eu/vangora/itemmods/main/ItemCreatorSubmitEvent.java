package eu.vangora.itemmods.main;

import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import org.bukkit.inventory.ItemStack;

public interface ItemCreatorSubmitEvent {
    default void onEvent(ItemStack itemStack){

    }
}
