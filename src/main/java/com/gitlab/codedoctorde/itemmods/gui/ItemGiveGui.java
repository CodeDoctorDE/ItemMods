package com.gitlab.codedoctorde.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.GuiPage;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.gitlab.codedoctorde.itemmods.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemGiveGui {
    private ItemStackBuilder itemStackBuilder;

    public ItemGiveGui(ItemStack itemStack) {
        this.itemStackBuilder = new ItemStackBuilder(itemStack);
    }

    public ItemGiveGui(ItemStackBuilder itemStackBuilder) {
        this.itemStackBuilder = itemStackBuilder;
    }

    public Gui createGui(Gui backGui) {
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "give");
        return new Gui(Main.getPlugin()) {{
            getGuiPages().add(new GuiPage(guiTranslation.getString("title"), 3) {{
                getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        backGui.open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 + 4, new GuiItem(itemStackBuilder.build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
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
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 2 + 4, new GuiItem(Main.translateItem(guiTranslation.getSection("info")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
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
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(9 * 2 + 8, new GuiItem(Main.translateItem(guiTranslation.getSection("give")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().getInventory().addItem(itemStackBuilder.build());
                    }
                }));
            }});
        }};
    }

}
