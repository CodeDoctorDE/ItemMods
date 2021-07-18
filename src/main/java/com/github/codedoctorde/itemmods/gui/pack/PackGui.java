package com.github.codedoctorde.itemmods.gui.pack;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.GuiCollection;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.PacksGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;

public class PackGui extends GuiCollection {
    public PackGui(String name) {
        var t = ItemMods.getTranslationConfig().subTranslation("gui.pack");
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        if (!pack.isEditable()) {
            registerItem(4, 2, new TranslatedGuiItem(new ItemStackBuilder(Material.STRUCTURE_VOID).displayName("readonly.title").lore("readonly.description").build()));
            return;
        }
        var placeholderItem = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build());
        for (PackTab value : PackTab.values()) {
            TranslatedChestGui gui = new TranslatedChestGui(t, 4);
            gui.setPlaceholders(name);
            gui.fillItems(0, 0, 0, 3, placeholderItem);
            gui.fillItems(8, 0, 8, 3, placeholderItem);
            gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
                setClickAction(event -> new PacksGui().show((Player) event.getWhoClicked()));
            }});
            gui.addItem(placeholderItem);
            Arrays.stream(PackTab.values()).map(tab -> new TranslatedGuiItem(new ItemStackBuilder(tab.getMaterial()).displayName(tab.name().toLowerCase())
                    .setEnchanted(tab == value).build()) {{
                setClickAction(event -> setCurrent(tab.ordinal()));
            }}).forEach(gui::addItem);
            gui.fillItems(0, 0, 8, 1, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            switch (value) {
                case ADMINISTRATION:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder().build()) {{
                        setRenderAction(gui -> {
                            var activated = ItemMods.getPackManager().isActivated(pack.getName());
                            var prefix = activated ? "activated." : "deactivated.";
                            setItemStack(new ItemStackBuilder(activated ? Material.GREEN_BANNER : Material.RED_BANNER).displayName(prefix + "title").lore(prefix + "description").build());
                        });
                        setClickAction(event -> {
                            if (ItemMods.getPackManager().isActivated(pack.getName()))
                                ItemMods.getPackManager().deactivatePack(pack.getName());
                            else
                                ItemMods.getPackManager().activatePack(pack.getName());
                            reloadAll();
                        });
                    }});
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.CHEST).displayName("export.title").lore("export.description").build()) {{
                        setClickAction(event -> {
                            try {
                                ItemMods.getPackManager().export(pack.getName());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            event.getWhoClicked().sendMessage(t.getTranslation("export.message", "plugins/ItemMods/exports/" + pack.getName()));
                        });
                    }});
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("delete.title").lore("delete.description").build()) {{
                        setClickAction(event -> ItemMods.getPackManager().deletePack(pack.getName()));
                    }});
                    break;
                case GENERAL:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NAME_TAG).displayName("name.title").lore("name.description").build()) {{
                        setRenderAction((gui) -> setPlaceholders(pack.getName()));
                        setClickAction((event) -> {
                            var p = (Player) event.getWhoClicked();
                            hide(p);
                            var request = new ChatRequest(p);
                            p.sendMessage(t.getTranslation("name.message"));
                            request.setSubmitAction(s -> {
                                pack.setName(s);
                                p.sendMessage(t.getTranslation("name.success", s));
                                show(p);
                            });
                        });
                    }});
                    break;
                case CONTENTS:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND).displayName("items.title").lore("items.description").build()) {{
                        setClickAction(event -> new ItemsGui(name).show((Player) event.getWhoClicked()));
                    }});
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.GRASS_BLOCK).displayName("blocks.title").lore("blocks.description").build()) {{
                        setClickAction(event -> new BlocksGui(name).show((Player) event.getWhoClicked()));
                    }});
                    break;
                case RAW:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ITEM_FRAME).displayName("textures.title").lore("textures.description").build()) {{
                        setClickAction(event -> event.getWhoClicked().sendMessage(ItemMods.getTranslationConfig().getTranslation("coming-soon")));
                    }});
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ARMOR_STAND).displayName("models.title").lore("models.description").build()) {{
                        setClickAction(event -> event.getWhoClicked().sendMessage(ItemMods.getTranslationConfig().getTranslation("coming-soon")));
                    }});
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NOTE_BLOCK).displayName("sounds.title").lore("sounds.description").build()) {{
                        setClickAction(event -> event.getWhoClicked().sendMessage(ItemMods.getTranslationConfig().getTranslation("coming-soon")));
                    }});
                    break;
            }
            gui.fillItems(0, 0, 8, 1, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            gui.fillItems(0, 3, 8, 3, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            registerGui(gui);
        }
    }

    enum PackTab {
        GENERAL, CONTENTS, RAW, ADMINISTRATION;

        public Material getMaterial() {
            switch (this) {
                case ADMINISTRATION:
                    return Material.COMMAND_BLOCK;
                case GENERAL:
                    return Material.ITEM_FRAME;
                case CONTENTS:
                    return Material.BOOK;
                case RAW:
                    return Material.APPLE;
            }
            return Material.AIR;
        }
    }
}
