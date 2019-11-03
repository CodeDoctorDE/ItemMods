package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.ui.*;
import eu.vangora.itemmods.config.ItemConfig;
import eu.vangora.itemmods.main.ArmorType;
import eu.vangora.itemmods.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.text.MessageFormat;

public class ItemGui {
    private final int index;

    public ItemGui(int index) {
        this.index = index;
    }

    public Gui createGui(Gui backGui) {
        ItemConfig itemConfig = Main.getPlugin().getMainConfig().getItems().get(index);
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui","item");
        return new Gui(Main.getPlugin()){{
            getGuiPages().add(new GuiPage(MessageFormat.format(guiTranslation.getString("title"),itemConfig.getName(), index),5, new GuiEvent(){
                @Override
                public void onClose(Gui gui, GuiPage guiPage, Player player) {
                    Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
                }
            }){{
                getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        backGui.open(player);
                    }
                }));
                getGuiItems().put(9+1, new GuiItem((itemConfig.getItemStack() != null)?itemConfig.getItemStack():Main.translateItem(guiTranslation.getSection("item","null")).build(), new GuiItemEvent() {

                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        ItemStack change = event.getWhoClicked().getItemOnCursor();
                        if(change.getType().isEmpty() && itemConfig.getItemStack() == null){
                            itemConfig.setItemStack(new ItemStack(Material.PLAYER_HEAD));
                            try {
                                Main.getPlugin().saveBaseConfig();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            createGui(backGui).open((Player) event.getWhoClicked());

                        }else {
                            if (change.getType().isEmpty())
                                itemConfig.setItemStack(null);
                            else
                                itemConfig.setItemStack(change);
                            try {
                                Main.getPlugin().saveBaseConfig();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            createGui(backGui).open((Player) event.getWhoClicked());
                        }
                    }
                }));
                getGuiItems().put(9*4+4, new GuiItem(Main.translateItem(guiTranslation.getSection("get")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        if(itemConfig.getItemStack() == null) {
                            player.sendMessage(guiTranslation.getString("get", "null"));
                            return;
                        }
                        event.getWhoClicked().getInventory().addItem(itemConfig.getItemStack().clone());
                        event.getWhoClicked().sendMessage(guiTranslation.getString("get","success"));
                    }
                }));
                getGuiItems().put(9+3, new GuiItem(Main.translateItem(itemConfig.getItemStack() != null ?guiTranslation.getSection("creator","item"):guiTranslation.getSection("creator","null")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {

                    }
                }));
                getGuiItems().put(9+5, new GuiItem(Main.translateItem(itemConfig.isCanRename() ?guiTranslation.getSection("rename","yes"):guiTranslation.getSection("rename","no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        itemConfig.setCanRename(!itemConfig.isCanRename());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(itemConfig.isCanRename())
                            event.getWhoClicked().sendMessage(guiTranslation.getString("rename","yes","success"));
                        else
                            event.getWhoClicked().sendMessage(guiTranslation.getString("rename","no","success"));
                        createGui(backGui).open(player);
                    }
                }));

                getGuiItems().put(9+7, new GuiItem(Main.translateItem(itemConfig.isBoneMeal() ?guiTranslation.getSection("bonemeal","yes"):guiTranslation.getSection("bonemeal","no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        itemConfig.setBoneMeal(!itemConfig.isBoneMeal());
                        try {
                            Main.getPlugin().saveBaseConfig();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(itemConfig.isBoneMeal())
                            event.getWhoClicked().sendMessage(guiTranslation.getString("bonemeal","yes","success"));
                        else
                            event.getWhoClicked().sendMessage(guiTranslation.getString("bonemeal","no","success"));
                        createGui(backGui).open(player);
                    }
                }));
            }});
        }};
    }
}
