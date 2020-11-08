package com.github.codedoctorde.itemmods.gui.block.choose;

import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.events.GuiListEvent;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.google.gson.JsonObject;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;

/**
 * @author CodeDoctorDE
 */
public class ChooseBlockConfigGui {
    private final ChooseBlockConfigEvent blockConfigEvent;

    public ChooseBlockConfigGui(ChooseBlockConfigEvent blockConfigEvent) {
        this.blockConfigEvent = blockConfigEvent;
    }

    public Gui[] createGui(Gui backGui) {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("choose").getAsJsonObject("block").getAsJsonObject("config");
        return new ListGui(guiTranslation, ItemMods.getPlugin(), new GuiListEvent() {
            @Override
            public String title(int index, int size) {
                return MessageFormat.format(guiTranslation.get("title").getAsString(), index + 1, size);
            }

            @Override
            public GuiItem[] pages(String s) {
                return ItemMods.getPlugin().getMainConfig().getBlocks().stream().filter(blockConfig -> blockConfig.getNamespace().contains(s) || blockConfig.getName().contains(s)).map(blockConfig -> new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("config")).format(
                        blockConfig.getDisplayName(), blockConfig.getName(), blockConfig.getNamespace()), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        blockConfigEvent.onEvent(blockConfig);
                    }
                })).toArray(GuiItem[]::new);
            }
        }, new GuiEvent() {
        }).createGuis(backGui);
    }

    public interface ChooseBlockConfigEvent {
        void onEvent(BlockConfig itemConfig);
    }
}
