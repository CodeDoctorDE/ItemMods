package com.github.codedoctorde.itemmods.gui.block.choose;

import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.events.GuiListEvent;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.api.CustomTemplateData;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.gui.block.BlockGui;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * @author CodeDoctorDE
 */
public class ChooseBlockTemplateGui {
    private final int blockIndex;
    private final ItemModsAddon addon;

    public ChooseBlockTemplateGui(int blockIndex, ItemModsAddon addon) {
        this.blockIndex = blockIndex;
        this.addon = addon;
    }

    public Gui[] createGuis() {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("choose").getAsJsonObject("blocktemplate");
        BlockConfig blockConfig = ItemMods.getPlugin().getMainConfig().getBlocks().get(blockIndex);
        return new ListGui(guiTranslation, ItemMods.getPlugin(), new GuiListEvent() {
            @Override
            public String title(int index, int size) {
                return MessageFormat.format(guiTranslation.get("title").getAsString(), blockConfig.getName(), blockIndex, addon.getName(), index + 1);
            }

            @Override
            public GuiItem[] pages(String s) {
                return Arrays.stream(addon.getBlockTemplates()).filter(blockTemplate -> blockTemplate.getName().contains(s)).map(blockTemplate -> new GuiItem(blockTemplate.createIcon(blockConfig), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        ItemMods.getPlugin().getMainConfig().getBlocks().get(blockIndex).setTemplate(new CustomTemplateData(blockTemplate));
                        ItemMods.getPlugin().saveBaseConfig();
                        new BlockGui(blockIndex).createGui().open((Player) event.getWhoClicked());
                    }
                })).toArray(GuiItem[]::new);
            }
        }, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }).createGuis(new BlockGui(blockIndex).createGui());
    }
}
