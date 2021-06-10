package com.github.codedoctorde.itemmods.gui.item;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.MessageGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class ItemsGui extends ListGui {

    public ItemsGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.items"), (s, translation) -> ItemMods.getMainConfig().getItems().stream().map(itemConfig -> new StaticItem(new ItemStackBuilder(itemConfig.getItemStack()).build()) {{
            setClickAction(event -> {
                Player player = (Player) event.getWhoClicked();
                ClickType clickType = event.getClick();
                switch (clickType) {
                    case LEFT:
                        new ItemGui(itemConfig.getIdentifier()).show(player);
                        break;
                    case DROP:
                        List<ItemConfig> itemConfigs = ItemMods.getMainConfig().getItems();
                        Translation t = ItemMods.getTranslationConfig().subTranslation("gui.items.delete");
                        new MessageGui(t) {{
                            addActions(new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_BANNER).setDisplayName("yes").build()) {{
                                setClickAction(event -> {
                                    itemConfigs.remove(itemConfig);
                                    ItemMods.saveBaseConfig();
                                });
                            }});
                            addActions(new TranslatedGuiItem(new ItemStackBuilder(Material.RED_BANNER).setDisplayName("no").build()) {{
                                setClickAction(event -> show(player));
                            }});
                        }}.show(player);
                        break;
                }
            });
        }}).toArray(GuiItem[]::new));
        Translation t = getTranslation();
        setListControls(new VerticalListControls(3) {{
            setCreateAction((event) -> {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage(t.getTranslation("create.message"));
                hide(player);
                ChatRequest request = new ChatRequest(player);
                request.setSubmitAction(s -> {
                    String output = ChatColor.translateAlternateColorCodes('&', s);
                    ItemMods.getMainConfig().getItems().add(new ItemConfig("itemmods", output));
                    ItemMods.saveBaseConfig();
                    player.sendMessage(t.getTranslation("create.success", output));
                    rebuild();
                    show(player);
                });
                request.setCancelAction(() -> player.sendMessage(t.getTranslation("create.cancel")));
            });
        }});
    }
}
