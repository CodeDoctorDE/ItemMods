package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.request.ChatRequest;
import com.gitlab.codedoctorde.api.request.ChatRequestEvent;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.GuiPage;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import eu.vangora.itemmods.config.ItemConfig;
import eu.vangora.itemmods.main.ItemCreatorSubmitEvent;
import eu.vangora.itemmods.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

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
                        submitEvent.onEvent(itemStack);
                    }
                }));
                getGuiItems().put(9, new GuiItem(Main.translateItem(guiTranslation.getSection("displayname")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                            @Override
                            public void onEvent(Player player, String output) {
                                output = ChatColor.translateAlternateColorCodes('&', output);
                                player.sendMessage(MessageFormat.format(guiTranslation.getString("displayname", "success"), output));
                                createGui(backGui).open(player);
                            }

                            @Override
                            public void onCancel(Player player) {
                                player.sendMessage(guiTranslation.getString("displayname", "cancel"));
                            }
                        });
                    }
                }));
                getGuiItems().put(10, new GuiItem(Main.translateItem(guiTranslation.getSection("lore")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {
                        switch (event.getClick()){
                            case LEFT:
                                new ChatRequest(Main.getPlugin(), (Player) event.getWhoClicked(), new ChatRequestEvent() {
                                    @Override
                                    public void onEvent(Player player, String output) {
                                        output = ChatColor.translateAlternateColorCodes('&', output);
                                        if(itemStack.getLore() == null)
                                            itemStack.setLore(new ArrayList<>());
                                        itemStack.getLore().add(output);
                                        player.sendMessage(MessageFormat.format(guiTranslation.getString("lore", "success"), output));
                                        createGui(backGui).open(player);
                                    }

                                    @Override
                                    public void onCancel(Player player) {
                                        player.sendMessage(guiTranslation.getString("lore", "cancel"));
                                    }
                                });
                                break;
                            case SHIFT_LEFT:
                                if(itemStack.getLore() == null)
                                    itemStack.setLore(new ArrayList<>());
                                itemStack.getLore().add("");
                                event.getWhoClicked().sendMessage(guiTranslation.getString("lore", "empty"));
                                break;
                            case RIGHT:
                                if(itemStack.getLore() == null)
                                    itemStack.setLore(new ArrayList<>());
                                if(itemStack.getLore().size() > 0)
                                    itemStack.getLore().remove(itemStack.getLore().size());
                                event.getWhoClicked().sendMessage(guiTranslation.getString("lore", "remove"));
                                break;
                            case SHIFT_RIGHT:
                                itemStack.setLore(new ArrayList<>());
                            case DROP:
                                if(itemStack.getLore() != null)
                                    event.getWhoClicked().sendMessage(MessageFormat.format(guiTranslation.getString("lore","get"),
                                            String.join(guiTranslation.getString("lore","delimiter"), itemStack.getLore())));
                                else
                                    event.getWhoClicked().sendMessage(guiTranslation.getString("lore","null"));
                        }
                    }
                }));
            }});
        }};
    }
}
