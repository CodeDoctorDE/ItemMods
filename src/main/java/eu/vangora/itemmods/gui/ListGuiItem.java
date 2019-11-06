package eu.vangora.itemmods.gui;

import com.gitlab.codedoctorde.api.ui.GuiItem;
import com.gitlab.codedoctorde.api.ui.GuiItemEvent;
import org.bukkit.inventory.ItemStack;

public class ListGuiItem extends GuiItem {
    private String name;

    public ListGuiItem(ItemStack itemStack, GuiItemEvent guiItemEvent, String name) {
        super(itemStack, guiItemEvent);
        this.name = name;
    }

    public ListGuiItem(GuiItemEvent guiItemEvent, String name) {
        super(guiItemEvent);
        this.name = name;
    }

    public ListGuiItem(ItemStack itemStack, String name) {
        super(itemStack);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
