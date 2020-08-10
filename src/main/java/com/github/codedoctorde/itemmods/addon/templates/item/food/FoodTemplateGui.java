package com.github.codedoctorde.itemmods.addon.templates.item.food;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.github.codedoctorde.itemmods.gui.item.ItemGui;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.ui.GuiEvent;
import com.github.codedoctorde.api.ui.GuiItem;
import com.github.codedoctorde.api.ui.GuiItemEvent;
import com.github.codedoctorde.api.ui.template.item.ValueItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FoodTemplateGui {
    private final FoodTemplate template;
    private final ItemConfig itemConfig;
    private final FoodTemplate.FoodTemplateData data;
    JsonObject guiTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("addon")
            .getAsJsonObject("templates").getAsJsonObject("item").getAsJsonObject("food").getAsJsonObject("gui");

    public FoodTemplateGui(FoodTemplate template, ItemConfig itemConfig){
        this.template = template;
        this.itemConfig = itemConfig;
        this.data = new FoodTemplate.FoodTemplateData(template, itemConfig);
    }
    public Gui createGui(){
        return new Gui(ItemMods.getPlugin(), guiTranslation.get("title").getAsString(), 3, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                player.sendMessage(guiTranslation.getAsJsonObject("cancel").get("output").getAsString());
            }
        }){{
            getGuiItems().put(0, new GuiItem(guiTranslation.getAsJsonObject("cancel"), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("cancel").get("output").getAsString());
                    new ItemGui(itemConfig).createGui();
                }
            }));
            getGuiItems().put(9 + 2, new ValueItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("level")), data.getLevel(), 1, (i, player, valueItem) -> {
                data.setExhaustion(i);
                if(i < 0 || i > 100)
                    return false;
                data.setLevel((int) i);
                return true;
            }).build());
            getGuiItems().put(9 + 4, new ValueItem(new ItemStackBuilder(guiTranslation.getAsJsonObject("exhaustion")), data.getExhaustion(), 1, (i, player, valueItem) -> {
                if(i < 0)
                    return false;
                data.setExhaustion(i);
                return true;
            }).setSkip(0.1f).setFastSkip(1).build());
            getGuiItems().put(9 + 6, new GuiItem(guiTranslation.getAsJsonObject("")));
            getGuiItems().put(9 * 2 + 8, new GuiItem(guiTranslation.getAsJsonObject("save"), new GuiItemEvent() {
                @Override
                public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                    data.save();
                    new ItemGui(itemConfig).createGui();
                }
            }));
        }};
    }
}
