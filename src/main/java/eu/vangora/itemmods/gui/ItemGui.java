package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.*;
import eu.vangora.itemmods.config.ItemConfig;
import eu.vangora.itemmods.main.ItemCreatorSubmitEvent;
import eu.vangora.itemmods.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                        new ItemCreatorGui(itemConfig.getItemStack(), new ItemCreatorSubmitEvent() {
                            @Override
                            public void onEvent(ItemStack itemStack) {
                                itemConfig.setItemStack(itemStack);
                                try {
                                    Main.getPlugin().saveBaseConfig();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                createGui(backGui).open((Player) event.getWhoClicked());
                            }
                        }).createGui(gui).open((Player) event.getWhoClicked());
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

                getGuiItems().put(9*3+1, new GuiItem(Main.translateItem(guiTranslation.getSection("events")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        Player player = (Player) event.getWhoClicked();
                        createEventsGui(gui).open(player);
                    }
                }));
            }});
        }};
    }

    private Gui createEventsGui(Gui backGui) {
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui","item","events");
        ItemConfig itemConfig = Main.getPlugin().getMainConfig().getItems().get(index);
        return new Gui(Main.getPlugin()){{
           getGuiPages().add(new GuiPage(guiTranslation.getString("title"), 5, new GuiEvent() {
               @Override
               public void onClose(Gui gui, GuiPage guiPage, Player player) {
                   Main.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
               }
           }){{
               getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {
                   @Override
                   public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        backGui.open((Player) event.getWhoClicked());
                   }
               }));
               getGuiItems().put(9+1, new GuiItem(Main.translateItem(guiTranslation.getSection("helmet")).build(), new GuiItemEvent() {
                   @Override
                   public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                       new ListGui(itemConfig.getOnHelmet().stream().map(command -> new ListGuiItem(Main.translateItem(guiTranslation.getSection("helmet", "item")).build(), new GuiItemEvent() {
                           @Override
                           public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                               itemConfig.getOnHelmet().remove(command);
                           }
                       }, command)).collect(Collectors.toList()), new GuiItemEvent() {
                           @Override
                           public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                               gui.close((Player) event.getWhoClicked());
                               event.getWhoClicked().sendMessage(guiTranslation.getString("helmet","create","message"));
                               new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                   @Override
                                   public void onEvent(Player player, String output) {
                                       player.sendMessage(guiTranslation.getString("helmet","create","success"));
                                       itemConfig.getOnHelmet().add(output);
                                       createEventsGui(backGui).open(player);
                                   }

                                   @Override
                                   public void onCancel(Player player) {
                                       player.sendMessage(guiTranslation.getString("helmet","create","cancel"));
                                   }
                               });
                           }
                       }).createGui(gui).open((Player) event.getWhoClicked());
                   }
               }));
               getGuiItems().put(9+5, new GuiItem(Main.translateItem(guiTranslation.getSection("chestplate")).build(), new GuiItemEvent() {
                   @Override
                   public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                       new ListGui(itemConfig.getOnHelmet().stream().map(command -> new ListGuiItem(Main.translateItem(guiTranslation.getSection("chestplate", "item")).build(), new GuiItemEvent() {
                           @Override
                           public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                               itemConfig.getOnChestplate().remove(command);
                           }
                       }, command)).collect(Collectors.toList()), new GuiItemEvent() {
                           @Override
                           public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                               gui.close((Player) event.getWhoClicked());
                               event.getWhoClicked().sendMessage(guiTranslation.getString("chestplate","create","message"));
                               new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                   @Override
                                   public void onEvent(Player player, String output) {
                                       player.sendMessage(guiTranslation.getString("chestplate","create","success"));
                                       itemConfig.getOnChestplate().add(output);
                                       createEventsGui(backGui).open(player);
                                   }

                                   @Override
                                   public void onCancel(Player player) {
                                       player.sendMessage(guiTranslation.getString("helmet","create","cancel"));
                                   }
                               });
                           }
                       }).createGui(gui).open((Player) event.getWhoClicked());
                   }
               }));
               getGuiItems().put(9+7, new GuiItem(Main.translateItem(guiTranslation.getSection("leggings")).build(), new GuiItemEvent() {
                   @Override
                   public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                       new ListGui(itemConfig.getOnHelmet().stream().map(command -> new ListGuiItem(Main.translateItem(guiTranslation.getSection("leggings", "item")).build(), new GuiItemEvent() {
                           @Override
                           public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                               itemConfig.getOnLeggings().remove(command);
                           }
                       }, command)).collect(Collectors.toList()), new GuiItemEvent() {
                           @Override
                           public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                               gui.close((Player) event.getWhoClicked());
                               event.getWhoClicked().sendMessage(guiTranslation.getString("leggings","create","message"));
                               new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                   @Override
                                   public void onEvent(Player player, String output) {
                                       player.sendMessage(guiTranslation.getString("leggings","create","success"));
                                       itemConfig.getOnLeggings().add(output);
                                       createEventsGui(backGui).open(player);
                                   }

                                   @Override
                                   public void onCancel(Player player) {
                                       player.sendMessage(guiTranslation.getString("helmet","create","cancel"));
                                   }
                               });
                           }
                       }).createGui(gui).open((Player) event.getWhoClicked());
                   }
               }));
               getGuiItems().put(9*3+1, new GuiItem(Main.translateItem(guiTranslation.getSection("boots")).build(), new GuiItemEvent() {
                   @Override
                   public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                       new ListGui(itemConfig.getOnHelmet().stream().map(command -> new ListGuiItem(Main.translateItem(guiTranslation.getSection("boots", "item")).build(), new GuiItemEvent() {
                           @Override
                           public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                               itemConfig.getOnBoots().remove(command);
                           }
                       }, command)).collect(Collectors.toList()), new GuiItemEvent() {
                           @Override
                           public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                               gui.close((Player) event.getWhoClicked());
                               event.getWhoClicked().sendMessage(guiTranslation.getString("boots","create","message"));
                               new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                   @Override
                                   public void onEvent(Player player, String output) {
                                       player.sendMessage(guiTranslation.getString("boots","create","success"));
                                       itemConfig.getOnLeggings().add(output);
                                       createEventsGui(backGui).open(player);
                                   }

                                   @Override
                                   public void onCancel(Player player) {
                                       player.sendMessage(guiTranslation.getString("boots","create","cancel"));
                                   }
                               });
                           }
                       }).createGui(gui).open((Player) event.getWhoClicked());
                   }
               }));
           }});
        }};
    }
}
