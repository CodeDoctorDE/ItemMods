package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.GuiPage;
import eu.vangora.itemmods.main.ItemCreatorSubmitEvent;
import eu.vangora.itemmods.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemCreatorGui {

    private ItemStack itemStack;
    private ItemCreatorSubmitEvent submitEvent;

    public ItemCreatorGui(ItemStack itemStack, ItemCreatorSubmitEvent submitEvent){
        this.itemStack = itemStack;
        this.submitEvent = submitEvent;
    }
    public Gui createGui(Gui backGui){
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "itemcreator");
        return new Gui(Main.getPlugin()){{
            getGuiPages().add(new GuiPage(guiTranslation.getString("title"),5){{
                GuiItem placeholder = new GuiItem(Main.translateItem(guiTranslation.getSection("placeholder")).build());
                getGuiItems().put(0, new GuiItem(Main.translateItem(guiTranslation.getSection("back")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        backGui.open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(1, placeholder);
                getGuiItems().put(2, placeholder);
                getGuiItems().put(3, placeholder);
                getGuiItems().put(4, new GuiItem(itemStack));
                getGuiItems().put(5, placeholder);
                getGuiItems().put(6, placeholder);
                getGuiItems().put(7, placeholder);
                getGuiItems().put(8, new GuiItem(Main.translateItem(guiTranslation.getSection("submit")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getString("submit", "success"));
                        submitEvent.onEvent(itemStack);
                    }
                }));
                getGuiItems().put(9, new GuiItem(Main.translateItem(guiTranslation.getSection("displayname")).format(itemStack.getItemMeta().getDisplayName()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()) {
                            case LEFT:
                                gui.close((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("displayname", "message"));
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        output = ChatColor.translateAlternateColorCodes('&', output);
                                        ItemMeta itemMeta = itemStack.getItemMeta();
                                        itemMeta.setDisplayName(output);
                                        itemStack.setItemMeta(itemMeta);
                                        player.sendMessage(MessageFormat.format(guiTranslation.getString("displayname", "success"), output));
                                        createGui(backGui).open(player);
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        player.sendMessage(guiTranslation.getString("displayname", "cancel"));
                                        createGui(backGui).open(player);
                                    }
                                });
                                break;
                            case RIGHT:
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                itemMeta.setDisplayName(null);
                                itemStack.setItemMeta(itemMeta);
                                event.getWhoClicked().sendMessage(guiTranslation.getString("displayname", "remove"));
                                createGui(backGui).open((Player) event.getWhoClicked());
                                break;
                        }
                    }
                }));
                getGuiItems().put(10, new GuiItem(Main.translateItem(guiTranslation.getSection("lore")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta.getLore() == null)
                            itemMeta.setLore(new ArrayList<>());
                        List<String> lore = itemMeta.getLore();
                        switch (event.getClick()){
                            case LEFT:
                                gui.close((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("lore", "message"));
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        output = ChatColor.translateAlternateColorCodes('&', output);
                                        ItemMeta itemMeta = itemStack.getItemMeta();
                                        if (itemMeta.getLore() == null)
                                            itemMeta.setLore(new ArrayList<>());
                                        List<String> lore = itemMeta.getLore();
                                        lore.add(output);
                                        itemMeta.setLore(lore);
                                        itemStack.setItemMeta(itemMeta);
                                        player.sendMessage(MessageFormat.format(guiTranslation.getString("lore", "success"), output));
                                        createGui(backGui).open(player);
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        player.sendMessage(guiTranslation.getString("lore", "cancel"));
                                        createGui(backGui).open(player);
                                    }
                                });
                                break;
                            case SHIFT_LEFT:
                                lore.add(" ");
                                event.getWhoClicked().sendMessage(guiTranslation.getString("lore", "empty"));
                                itemMeta.setLore(lore);
                                itemStack.setItemMeta(itemMeta);
                                createGui(backGui).open((Player) event.getWhoClicked());
                                break;
                            case RIGHT:
                                if (!lore.isEmpty())
                                    lore.remove(lore.size() - 1);
                                event.getWhoClicked().sendMessage(guiTranslation.getString("lore", "remove"));
                                itemMeta.setLore(lore);
                                itemStack.setItemMeta(itemMeta);
                                createGui(backGui).open((Player) event.getWhoClicked());
                                break;
                            case SHIFT_RIGHT:
                                lore.clear();
                            case DROP:
                                if (lore.isEmpty())
                                    event.getWhoClicked().sendMessage(guiTranslation.getString("lore", "null"));
                                else
                                    event.getWhoClicked().sendMessage(MessageFormat.format(guiTranslation.getString("lore", "get"),
                                            String.join(guiTranslation.getString("lore", "delimiter"), lore)));
                        }
                    }
                }));
            }});
        }};
    }
}
