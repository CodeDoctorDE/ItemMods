package com.github.codedoctorde.itemmods.gui.block;

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
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.gui.MainGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class BlocksGui extends ListGui {

    public BlocksGui() {
        super(ItemMods.getTranslationConfig().subTranslation("gui.items"), (s, translation) -> ItemMods.getMainConfig().getBlocks().stream().map(itemConfig -> new StaticItem(new ItemStackBuilder(Material.GRASS_BLOCK).build()) {{
            setClickAction(event -> {
                Player player = (Player) event.getWhoClicked();
                ClickType clickType = event.getClick();
                switch (clickType) {
                    case LEFT:
                        new BlockGui(itemConfig).show(player);
                        break;
                    case DROP:
                        List<BlockConfig> itemConfigs = ItemMods.getMainConfig().getBlocks();
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
        controlsOffset(7, 0);
        setListControls(new VerticalListControls() {{
            setBackAction((event) -> new MainGui());
            setCreateAction((event) -> {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage(t.getTranslation("create.message"));
                hide(player);
                ChatRequest request = new ChatRequest(player);
                request.setSubmitAction(s -> {
                    String output = ChatColor.translateAlternateColorCodes('&', s);
                    ItemMods.getMainConfig().getBlocks().add(new BlockConfig("itemmods", output));
                    ItemMods.saveBaseConfig();
                    player.sendMessage(t.getTranslation("create.success", output));
                    rebuild();
                    show(player);
                });
                request.setCancelAction(() -> player.sendMessage(t.getTranslation("create.cancel")));
            });
        }});
        rebuild();
    }
}
