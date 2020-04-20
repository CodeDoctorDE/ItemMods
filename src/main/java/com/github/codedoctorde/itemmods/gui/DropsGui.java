package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.config.DropConfig;
import com.github.codedoctorde.itemmods.main.Main;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiEvent;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.template.ItemCreatorGui;
import com.gitlab.codedoctorde.api.ui.template.ListGui;
import com.gitlab.codedoctorde.api.ui.template.events.GuiListEvent;
import com.gitlab.codedoctorde.api.ui.template.events.ItemCreatorSubmitEvent;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CodeDoctorDE
 */
public class DropsGui {
    private final int blockIndex;

    public DropsGui(int blockIndex) {
        this.blockIndex = blockIndex;
    }

    public Gui[] createGui() {
        return createGui(false);
    }

    public Gui[] createGui(boolean rarityEditing) {
        JsonObject guiTranslation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("drops");
        BlockConfig blockConfig = Main.getPlugin().getMainConfig().getBlocks().get(blockIndex);
        return new ListGui(Main.getPlugin(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {

            }
        }, new GuiListEvent() {
            @Override
            public String title(int index, int size) {
                return MessageFormat.format(guiTranslation.get("title").getAsString(), blockIndex, index, size);
            }

            @Override
            public GuiItem[] pages(String s) {
                List<GuiItem> guiItems = new ArrayList<>();
                for (DropConfig dropConfig :
                        blockConfig.getDrops()) {
                    guiItems.add(new GuiItem(new ItemStackBuilder(dropConfig.getItemStack().clone()).addLore(guiTranslation.getAsJsonObject("drop").getAsJsonObject((rarityEditing) ? "rarity" : "general").getAsJsonArray("lore")), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            if (event.getClick() == ClickType.MIDDLE) {
                                createGui(!rarityEditing)[0].open((Player) event.getWhoClicked());
                                return;
                            }
                            if (rarityEditing) {
                                int rarity = dropConfig.getRarity();
                                switch (event.getClick()) {
                                    case LEFT:
                                        rarity++;
                                        break;
                                    case RIGHT:
                                        rarity--;
                                        break;
                                    case SHIFT_LEFT:
                                        rarity += 5;
                                        break;
                                    case SHIFT_RIGHT:
                                        rarity -= 5;
                                        break;
                                    case DROP:
                                        rarity = 1;
                                }
                                if (rarity <= 0)
                                    return;
                                dropConfig.setRarity(rarity);
                                event.getWhoClicked().sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("drop").getAsJsonObject("rarity").get("success").getAsString(), rarity));
                                createGui(rarityEditing)[0].open((Player) event.getWhoClicked());
                            } else {
                                switch (event.getClick()) {
                                    case LEFT:
                                        new ItemCreatorGui(Main.getPlugin(), dropConfig.getItemStack(), new ItemCreatorSubmitEvent() {
                                            @Override
                                            public void onEvent(ItemStack itemStack) {
                                                dropConfig.setItemStack(itemStack);
                                            }
                                        }).createGui(gui, Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("itemcreator"));
                                        break;
                                }
                            }
                        }
                    }));
                }
                return guiItems.toArray(new GuiItem[0]);
            }
        }, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }).createGui(guiTranslation);
    }
}
