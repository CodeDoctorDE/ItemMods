package com.github.codedoctorde.itemmods.gui.pack.raw.model;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.GuiCollection;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.MaterialListGui;
import com.github.codedoctorde.api.ui.template.gui.MessageGui;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.raw.ModelsGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

public class ModelGui extends GuiCollection {
    public ModelGui(PackObject packObject) {
        super();
        var t = ItemMods.getTranslationConfig().subTranslation("gui.raw.model");
        var asset = packObject.getModel();
        assert asset != null;
        var empty = new StaticItem(new ItemStackBuilder().build());
        var placeholder = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName(" ").build());
        for (ModelTab value : ModelTab.values()) {
            var gui = new TranslatedChestGui(t, 4);
            gui.fillItems(0, 0, 0, 3, placeholder);
            gui.fillItems(8, 0, 8, 3, placeholder);
            gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
                setClickAction(event -> new ModelsGui(packObject.getNamespace()).show((Player) event.getWhoClicked()));
            }});
            gui.addItem(placeholder);
            Arrays.stream(ModelTab.values()).map(tab -> new TranslatedGuiItem(new ItemStackBuilder(tab.getMaterial()).displayName(tab.name().toLowerCase())
                    .setEnchanted(tab == value).build()) {{
                setClickAction(event -> setCurrent(tab.ordinal()));
            }}).forEach(gui::addItem);
            gui.fillItems(0, 0, 8, 0, placeholder);
            switch (value) {
                case GENERAL:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NAME_TAG).displayName("name.title").lore("name.description").build()) {{
                        setClickAction(event -> {
                            var p = (Player) event.getWhoClicked();
                            hide(p);
                            var request = new ChatRequest(p);
                            p.sendMessage(t.getTranslation("name.message"));
                            request.setSubmitAction(s -> {
                                try {
                                    asset.setName(s);
                                    packObject.save();
                                    show(p);
                                    p.sendMessage(t.getTranslation("name.success", s));
                                } catch (Exception e) {
                                    p.sendMessage(t.getTranslation("name.failed"));
                                    e.printStackTrace();
                                }
                            });
                        });
                    }});
                    break;
                case APPEARANCE:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder().build()) {{
                        setRenderAction(gui -> {
                            var icon = asset.getFallbackTexture();
                            if (icon == null)
                                icon = Material.ARMOR_STAND;
                            new ItemStackBuilder(icon).displayName("fallback.name").lore("fallback.description").build();
                        });
                        setClickAction(event -> new MaterialListGui(ItemMods.getTranslationConfig().subTranslation("gui.materials"), material -> {
                            asset.setFallbackTexture(material);
                            packObject.save();
                            show((Player) event.getWhoClicked());
                        }).show((Player) event.getWhoClicked()));
                    }});
                    break;
                case ADMINISTRATION:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("delete.title").lore("delete.description").build()) {{
                        setClickAction(event -> {
                            new MessageGui(t) {{
                                setActions(new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_BANNER).build()) {{
                                    setClickAction(event -> {
                                        Objects.requireNonNull(packObject.getPack()).unregisterModel(asset.getName());
                                        new ModelsGui(packObject.getNamespace()).show((Player) event.getWhoClicked());
                                    });
                                }}, new TranslatedGuiItem(new ItemStackBuilder(Material.RED_BANNER).build()) {{
                                    setClickAction(event -> show((Player) event.getWhoClicked()));
                                }});
                            }}.show((Player) event.getWhoClicked());

                        });
                    }});
                    break;
            }
            gui.fillItems(0, 0, 8, 1, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            gui.fillItems(0, 3, 8, 3, new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).build()));
            registerGui(gui);
        }
    }

    enum ModelTab {
        GENERAL, APPEARANCE, ADMINISTRATION;

        public Material getMaterial() {
            switch (this) {
                case ADMINISTRATION:
                    return Material.COMMAND_BLOCK;
                case GENERAL:
                    return Material.ITEM_FRAME;
                case APPEARANCE:
                    return Material.DIAMOND_SWORD;
            }
            return Material.AIR;
        }
    }
}
