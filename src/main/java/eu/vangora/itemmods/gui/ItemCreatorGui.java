package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import com.gitlab.codedoctorde.api.ui.GuiPage;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import eu.vangora.itemmods.config.ItemConfig;
import eu.vangora.itemmods.main.Main;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;

public class ItemCreatorGui {
    private int index;

    public ItemCreatorGui(int index){
        this.index = index;
    }
    public Gui createGui(Gui backGui){
        ItemConfig itemConfig = Main.getPlugin().getMainConfig().getItems().get(index);
        ItemStackBuilder itemStackBuilder = new ItemStackBuilder(itemConfig.getItemStack());
        JsonConfigurationSection guiTranslation = Main.getPlugin().getTranslationConfig().getSection("gui", "itemcreator");
        return new Gui(Main.getPlugin()){{
            getGuiPages().add(new GuiPage(MessageFormat.format(guiTranslation.getString("title"),index),5){{
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
                getGuiItems().put(4, new GuiItem(itemStackBuilder.build()));
                getGuiItems().put(5, placeholder);
                getGuiItems().put(6, placeholder);
                getGuiItems().put(7, placeholder);
                getGuiItems().put(8, placeholder);
                getGuiItems().put(9, new GuiItem(Main.translateItem(guiTranslation.getSection("displayname")).build(), new GuiItemEvent() {
                    @Override
                    public void onEvent(Gui gui, GuiPage guiPage, GuiItem guiItem, InventoryClickEvent event) {

                    }
                }));
            }});
        }};
    }
}
