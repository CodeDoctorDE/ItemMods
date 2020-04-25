package com.github.codedoctorde.itemmods.gui.choose.item.block;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.api.ItemModsAddon;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.gui.BlockGui;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.template.ListGui;
import com.gitlab.codedoctorde.api.ui.template.events.GuiListEvent;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.text.MessageFormat;

/**
 * @author CodeDoctorDE
 */
public class ChooseItemTemplateGui {
    private final int blockIndex;
    private final ItemModsAddon addon;

    public ChooseItemTemplateGui(int blockIndex, ItemModsAddon addon) {
        this.blockIndex = blockIndex;
        this.addon = addon;
    }

    public Gui[] createGui() {
        JsonObject guiTranslation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("choose").getAsJsonObject("blocktemplate");
        BlockConfig blockConfig = Main.getPlugin().getMainConfig().getBlocks().get(blockIndex);
        return new ListGui(Main.getPlugin(), new GuiListEvent() {
            @Override
            public String title(int index, int size) {
                return MessageFormat.format(guiTranslation.get("title").getAsString(), blockConfig.getName(), blockIndex, addon.getName(), index, size);
            }

            @Override
            public GuiItem[] pages(String s) {
                return addon.getBlockTemplates().stream().filter(blockTemplate -> blockTemplate.getName().contains(s)).map(blockTemplate -> new GuiItem(blockTemplate.getIcon(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                        Main.getPlugin().getMainConfig().getBlocks().get(blockIndex).setTemplate(blockTemplate);
                        Main.getPlugin().saveBaseConfig();
                        new BlockGui(blockIndex).createGui().open((Player) event.getWhoClicked());
                    }
                })).toArray(GuiItem[]::new);
            }
        }, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }).createGui(guiTranslation, new BlockGui(blockIndex).createGui());
    }
}
