package com.github.codedoctorde.itemmods.gui.block;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author CodeDoctorDE
 */
public class DropGui {
    private final int blockIndex;
    private final int dropIndex;

    public DropGui(int blockIndex, int dropIndex) {
        this.blockIndex = blockIndex;
        this.dropIndex = dropIndex;
    }

    public Gui createGui() {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("drop");
        BlockConfig blockConfig = ItemMods.getPlugin().getMainConfig().getBlocks().get(blockIndex);
        return new Gui(ItemMods.getPlugin(), guiTranslation.get("title").getAsString(), 3, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }) {{
            putGuiItem(0, new GuiItem(guiTranslation.getAsJsonObject("back"), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new DropsGui(blockIndex).createGui()[0].open((Player) event.getWhoClicked());
                }
            }));
        }};
    }
}
