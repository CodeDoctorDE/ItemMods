package com.github.codedoctorde.itemmods.gui.block;

import com.github.codedoctorde.api.request.ItemRequest;
import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.ListGui;
import com.github.codedoctorde.api.ui.template.gui.pane.list.VerticalListControls;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * @author CodeDoctorDE
 */
public class DropsGui extends ListGui {
    public DropsGui(String blockIdentifier) {
        super(ItemMods.getTranslationConfig().subTranslation("gui.drops"), (s, translation) -> Objects.requireNonNull(ItemMods.getMainConfig().getBlock(blockIdentifier)).getDrops().stream().map(dropConfig -> new StaticItem(new ItemStackBuilder(dropConfig.getItemStack()).build())).toArray(GuiItem[]::new));
        setListControls(new VerticalListControls(true) {{
            setCreateAction((event) -> {
                Translation t = getTranslation();
                event.getWhoClicked().sendMessage(t.getTranslation("create.message"));
                hide((Player) event.getWhoClicked());
                ItemRequest request = new ItemRequest((Player) event.getWhoClicked());
                request.setSubmitAction(itemStack -> {
                    Objects.requireNonNull(ItemMods.getMainConfig().getBlock(blockIdentifier)).getDrops().add(new DropConfig(itemStack));
                    show((Player) event.getWhoClicked());
                });
                request.setCancelAction(() -> show((Player) event.getWhoClicked()));
            });
        }});
        /*BlockConfig blockConfig = ItemMods.getMainConfig().getBlocks().get(blockIndex);
        return new ListGui(guiTranslation, ItemMods.getPlugin(), new GuiItemEvent() {
            @Override
            public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("create").get("message").getAsString());
                gui.close((Player) event.getWhoClicked());
                new ItemRequest(ItemMods.getPlugin(), (Player) event.getWhoClicked(), new RequestEvent<ItemStack>() {
                    @Override
                    public void onEvent(Player player, ItemStack itemStack) {
                        blockConfig.getDrops().add(new DropConfig(itemStack));
                        createGuis(rarityEditing)[0].open(player);
                        player.sendMessage(guiTranslation.getAsJsonObject("create").get("success").getAsString());
                    }

                    @Override
                    public void onCancel(Player player) {
                        player.sendMessage(guiTranslation.getAsJsonObject("create").get("cancel").getAsString());
                    }
                });
            }
        }, new GuiListEvent() {
            @Override
            public String title(int index, int size) {
                return MessageFormat.format(guiTranslation.get("title").getAsString(), blockIndex, index + 1, size);
            }

            @Override
            public GuiItem[] pages(String s) {
                List<GuiItem> guiItems = new ArrayList<>();
                for (int i = 0; i < blockConfig.getDrops().size(); i++) {
                    DropConfig dropConfig = blockConfig.getDrops().get(i);
                    int finalI = i;
                    List<String> lore = new ArrayList<>();
                    JsonArray lines;
                    if (rarityEditing)
                        lines = guiTranslation.getAsJsonObject("drop").getAsJsonObject("rarity").getAsJsonArray("lore");
                    else
                        lines = guiTranslation.getAsJsonObject("drop").getAsJsonObject("general").getAsJsonArray(dropConfig.isFortune() ? "fortune" : "nofortune");
                    for (JsonElement line :
                            lines)
                        if (rarityEditing)
                            lore.add(MessageFormat.format(line.getAsString(), dropConfig.getRarity()));
                        else
                            lore.add(line.getAsString());
                    guiItems.add(new GuiItem(new ItemStackBuilder(dropConfig.getItemStack().clone()).addLore(lore.toArray(new String[0])), new GuiItemEvent() {
                        @Override
                        public void onEvent(Gui gui, GuiItem guiItem, InventoryClickEvent event) {
                            if (event.getClick() == ClickType.MIDDLE) {
                                createGuis(!rarityEditing)[0].open((Player) event.getWhoClicked());
                                return;
                            }
                            if (rarityEditing) {
                                int rarity = dropConfig.getRarity();
                                switch (event.getClick()) {
                                    case LEFT:
                                        rarity++;
                                        break;
                                    case RIGHT:
                                        rarity--;
                                        break;
                                    case SHIFT_LEFT:
                                        rarity += 5;
                                        break;
                                    case SHIFT_RIGHT:
                                        rarity -= 5;
                                        break;
                                    case DROP:
                                        rarity = 100;
                                }
                                if (rarity < 0 || rarity > 100)
                                    return;
                                dropConfig.setRarity(rarity);
                                ItemMods.saveBaseConfig();
                                event.getWhoClicked().sendMessage(MessageFormat.format(guiTranslation.getAsJsonObject("drop").getAsJsonObject("rarity").get("success").getAsString(), blockIndex, rarity));
                                createGuis(true)[0].open((Player) event.getWhoClicked());
                            } else switch (event.getClick()) {
                                case LEFT:
                                    new ItemCreatorGui(ItemMods.getPlugin(), dropConfig.getItemStack(), new ItemCreatorEvent() {
                                        @Override
                                        public void onEvent(Player player, ItemStack itemStack) {
                                            dropConfig.setItemStack(itemStack);
                                            createGuis(false)[0].open((Player) event.getWhoClicked());
                                            ItemMods.saveBaseConfig();
                                        }

                                        @Override
                                        public void onCancel(Player player) {
                                            gui.open(player);
                                        }
                                    }).createGui(ItemMods.getTranslationConfig().getJsonObject().getAsJsonObject("gui").getAsJsonObject("itemcreator")).open((Player) event.getWhoClicked());
                                    break;
                                case RIGHT:
                                    event.getWhoClicked().getInventory().addItem(dropConfig.getItemStack());
                                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("drop").getAsJsonObject("general").get("give").getAsString());
                                    break;
                                case DROP:
                                    blockConfig.getDrops().remove(finalI);
                                    createGuis(false)[0].open((Player) event.getWhoClicked());
                                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("drop").getAsJsonObject("general").get("delete").getAsString());
                                    break;
                                case SHIFT_LEFT:
                                    dropConfig.setFortune(!dropConfig.isFortune());
                                    event.getWhoClicked().sendMessage(guiTranslation.getAsJsonObject("drop").getAsJsonObject("general").getAsJsonObject("fortune").get(dropConfig.isFortune() ? "yes" : "no").getAsString());
                            }
                        }
                    }));
                }
                return guiItems.toArray(new GuiItem[0]);
            }
        }, new GuiEvent() {
            @Override
            public void onClose(Gui gui, Player player) {
                ItemMods.getPlugin().getBaseCommand().getPlayerGuiHashMap().put(player, gui);
            }
        }).createGuis(new BlockGui(blockIndex).createGui());*/
    }
}
