package com.github.codedoctorde.itemmods.gui;

import com.github.codedoctorde.itemmods.ItemMods;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemGiveGui {
    private final ItemStackBuilder itemStackBuilder;

    public ItemGiveGui(ItemStack itemStack) {
        this.itemStackBuilder = new ItemStackBuilder(itemStack);
    }

    public ItemGiveGui(ItemStackBuilder itemStackBuilder) {
        this.itemStackBuilder = itemStackBuilder;
    }

    public Gui createGui(Gui backGui) {
        JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("give");
        return new Gui(ItemMods.getPlugin(), guiTranslation.get("title").getAsString(), 3) {{
            getGuiItems().put(0, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("back")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    backGui.open((Player) event.getWhoClicked());
                }
            }));
            getGuiItems().put(9 + 4, new GuiItem(itemStackBuilder.build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    switch (event.getClick()) {
                        case LEFT:
                            itemStackBuilder.amount(itemStackBuilder.getAmount() + 1);
                            break;
                        case SHIFT_LEFT:
                            itemStackBuilder.amount(itemStackBuilder.getAmount() + 5);
                            break;
                        case RIGHT:
                            itemStackBuilder.amount(itemStackBuilder.getAmount() - 1);
                            break;
                            case SHIFT_RIGHT:
                                itemStackBuilder.amount(itemStackBuilder.getAmount() - 5);
                                break;
                        }
                    gui.reload();
                    }
                }));
            getGuiItems().put(9 * 2 + 4, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("info")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    switch (event.getClick()) {
                        case LEFT:
                            itemStackBuilder.amount(itemStackBuilder.getAmount() + 1);
                            break;
                        case SHIFT_LEFT:
                            itemStackBuilder.amount(itemStackBuilder.getAmount() + 5);
                            break;
                        case RIGHT:
                                itemStackBuilder.amount(itemStackBuilder.getAmount() - 1);
                                break;
                            case SHIFT_RIGHT:
                                itemStackBuilder.amount(itemStackBuilder.getAmount() - 5);
                                break;
                        }
                        gui.reload();
                    }
                }));
            getGuiItems().put(9 * 2 + 8, new GuiItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("give")).build(), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    event.getWhoClicked().getInventory().addItem(itemStackBuilder.build());
                }
            }));
        }};
    }

}
