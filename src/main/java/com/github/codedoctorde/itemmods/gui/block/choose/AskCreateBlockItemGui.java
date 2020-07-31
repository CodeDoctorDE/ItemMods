package com.github.codedoctorde.itemmods.gui.block.choose;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.github.codedoctorde.itemmods.config.MainConfig;
import com.github.codedoctorde.itemmods.gui.block.BlocksGui;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author CodeDoctorDE
 */
public class AskCreateBlockItemGui {
    private final String name;

    public AskCreateBlockItemGui(String name) {
        this.name = name;
    }

    public Gui createGui() {
        MainConfig mainConfig = ItemMods.getPlugin().getMainConfig();
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("choose").getAsJsonObject("block").getAsJsonObject("askitem");
        ItemConfig itemConfig = new ItemConfig(name);
        return new Gui(ItemMods.getPlugin(), guiTranslation.get("title").getAsString(), 3) {{
            getGuiItems().put(9 + 3, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("yes")), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    BlockConfig blockConfig = new BlockConfig(name);
                    if (mainConfig.getItem(name) == null && mainConfig.getBlock(name) == null) {
                        mainConfig.getItems().add(itemConfig);
                        mainConfig.getBlocks().add(blockConfig);
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("yes").get("success").getAsString());
                    } else {
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("yes").get("already").getAsString());
                    }
                }
            }));
            getGuiItems().put(9 + 5, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("no")), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    if (mainConfig.getItem(name) == null) {
                        mainConfig.getItems().add(itemConfig);
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("no").get("success").getAsString());
                    } else {
                        event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("no").get("already").getAsString());
                    }
                }
            }));
            getGuiItems().put(0, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("back")), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    new BlocksGui().createGui()[0].open((Player) event.getWhoClicked());
                }
            }));
        }};
    }
}
