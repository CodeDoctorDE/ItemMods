package com.github.codedoctorde.itemmods.gui.block.choose;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.gui.block.BlockGui;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.template.gui.ListGui;
import com.gitlab.codedoctorde.api.ui.template.gui.events.GuiListEvent;
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

    public Gui[] createGui() {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("choose").getAsJsonObject("blocktemplate");
        BlockConfig blockConfig = ItemMods.getPlugin().getMainConfig().getBlocks().get(blockIndex);
        return new ListGui(ItemMods.getPlugin(), new GuiListEvent() {
            @Override
            public String title(int index, int size) {
                return MessageFormat.format(guiTranslation.get("title").getAsString(), blockConfig.getName(), blockIndex, addon.getName(), index + 1, size);
            }

            @Override
            public GuiItem[] pages(String s) {
                return Arrays.stream(addon.getBlockTemplates()).filter(blockTemplate -> blockTemplate.getName().contains(s)).map(blockTemplate -> new GuiItem(blockTemplate.getIcon(blockConfig), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        ItemMods.getPlugin().getMainConfig().getBlocks().get(blockIndex).setTemplate(blockTemplate);
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
        }).createGui(guiTranslation, new BlockGui(blockIndex).createGui());
    }
}
