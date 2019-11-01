package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.ui.*;
import eu.vangora.itemmods.config.ItemConfig;
import eu.vangora.itemmods.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;

public class ItemGui {
    private final int index;

    public ItemGui(int index) {
        this.index = index;
    }

    public Gui createGui(Player player, Gui backGui) {
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
                        Player player = (Player) event.getWhoClicked();
                        ItemStack change = event.getCursor();
                        player.getInventory().setItemInMainHand(change);
                        if(change == null)
                            itemConfig.setItemStack(null);
                        else if(change.getType().isEmpty())
                            itemConfig.setItemStack(null);
                        else if(!change.getType().isBlock() || change.getType() == Material.PLAYER_HEAD)
                            itemConfig.setItemStack(change);
                        else
                            player.sendMessage(guiTranslation.getString("item","invalid"));
                        createGui(player, backGui);
                    }
                }));
            }});
        }};
    }
}
