package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.GuiPage;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import eu.vangora.itemmods.main.ItemCreatorSubmitEvent;
import eu.vangora.itemmods.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemCreatorGui {

    private ItemStackBuilder itemStackBuilder;
    private ItemCreatorSubmitEvent submitEvent;

    public ItemCreatorGui(ItemStackBuilder itemStackBuilder, ItemCreatorSubmitEvent submitEvent){
        this.itemStackBuilder = itemStackBuilder;
        this.submitEvent = submitEvent;
    }
    public ItemCreatorGui(ItemStack itemStack, ItemCreatorSubmitEvent submitEvent){
        this.itemStackBuilder = new ItemStackBuilder(itemStack);
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
                getGuiItems().put(4, new GuiItem(itemStackBuilder.build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().getInventory().addItem(itemStackBuilder.build());
                    }
                }));
                getGuiItems().put(5, placeholder);
                getGuiItems().put(6, placeholder);
                getGuiItems().put(7, placeholder);
                getGuiItems().put(8, new GuiItem(Main.translateItem(guiTranslation.getSection("submit")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        event.getWhoClicked().sendMessage(guiTranslation.getString("submit", "success"));
                        submitEvent.onEvent(itemStackBuilder.build());
                    }
                }));
                getGuiItems().put(9, new GuiItem(Main.translateItem(guiTranslation.getSection("displayname")).format(itemStackBuilder.getDisplayName()).build(), new GuiItemEvent() {
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
                                        itemStackBuilder.displayName(output);
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
                                itemStackBuilder.displayName(null);
                                event.getWhoClicked().sendMessage(guiTranslation.getString("displayname", "remove"));
                                createGui(backGui).open((Player) event.getWhoClicked());
                                break;
                        }
                    }
                }));
                getGuiItems().put(10, new GuiItem(Main.translateItem(guiTranslation.getSection("lore")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        List<String> lore = itemStackBuilder.getLore();
                        if(lore == null)
                            lore = new ArrayList<>();
                        switch (event.getClick()){
                            case LEFT:
                                gui.close((Player) event.getWhoClicked());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("lore", "message"));
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        output = ChatColor.translateAlternateColorCodes('&', output);
                                        List<String> lore = itemStackBuilder.getLore();
                                        if(lore == null)
                                            lore = new ArrayList<>();
                                        lore.add(output);
                                        itemStackBuilder.lore(lore);
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
                                break;
                            case RIGHT:
                                if (!lore.isEmpty())
                                    lore.remove(lore.size() - 1);
                                event.getWhoClicked().sendMessage(guiTranslation.getString("lore", "remove"));
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
                        itemStackBuilder.setLore(lore);
                        if(event.getClick()!= ClickType.LEFT)
                            createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(11, new GuiItem(Main.translateItem(guiTranslation.getSection("amount")).format(itemStackBuilder.getAmount()).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        int amount = itemStackBuilder.getAmount();
                        switch (event.getClick()) {
                            case LEFT:
                                amount++;
                                break;
                            case RIGHT:
                                amount--;
                                break;
                            case SHIFT_LEFT:
                                amount += 5;
                                break;
                            case SHIFT_RIGHT:
                                amount -= 5;
                                break;
                        }
                        itemStackBuilder.setAmount(amount);
                        event.getWhoClicked().sendMessage(MessageFormat.format(guiTranslation.getString("amount", "success"), amount));
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
                getGuiItems().put(12, new GuiItem((itemStackBuilder.getCustomModelData() != null) ? Main.translateItem(guiTranslation.getSection("custommodeldata", "yes")).format(itemStackBuilder.getCustomModelData()).build() :
                        Main.translateItem(guiTranslation.getSection("custommodeldata", "no")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        if (itemStackBuilder.getCustomModelData() != null) {
                            Integer customModelData = itemStackBuilder.getCustomModelData();
                            switch (event.getClick()) {
                                case LEFT:
                                    customModelData++;
                                    break;
                                case RIGHT:
                                    customModelData--;
                                    break;
                                case SHIFT_LEFT:
                                    customModelData += 5;
                                    break;
                                case SHIFT_RIGHT:
                                    customModelData -= 5;
                                    break;
                                case DROP:
                                    customModelData = null;
                                    break;
                            }
                            itemStackBuilder.setCustomModelData(customModelData);
                            event.getWhoClicked().sendMessage(MessageFormat.format(guiTranslation.getString("custommodeldata", "success"), customModelData));
                        }else {
                            itemStackBuilder.setCustomModelData(0);
                            event.getWhoClicked().sendMessage(MessageFormat.format(guiTranslation.getString("custommodeldata", "success"), itemStackBuilder.getCustomModelData()));
                        }
                        createGui(backGui).open((Player) event.getWhoClicked());
                    }
                }));
            }});
        }};
    }
}
