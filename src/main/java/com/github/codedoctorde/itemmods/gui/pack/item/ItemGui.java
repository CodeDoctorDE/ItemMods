package com.github.codedoctorde.itemmods.gui.pack.item;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.GuiCollection;
import com.github.codedoctorde.api.ui.GuiPane;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.MessageGui;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.ChoosePackGui;
import com.github.codedoctorde.itemmods.gui.pack.ItemsGui;
import com.github.codedoctorde.itemmods.gui.pack.raw.ModelsGui;
import com.github.codedoctorde.itemmods.gui.pack.raw.model.ChooseModelGui;
import com.github.codedoctorde.itemmods.gui.pack.raw.model.ModelGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.ItemAsset;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.Objects;

public class ItemGui extends GuiCollection {
    protected final PackObject packObject;

    public ItemGui(PackObject packObject) {
        this.packObject = packObject;
        constructGuis();
    }

    protected void constructGuis() {
        var t = getTranslation();
        Arrays.stream(ItemTab.values()).map(tab -> new TranslatedChestGui(t, 4) {{
            addPane(buildTabs(tab.ordinal()));
            var pane = new GuiPane(7, 1);
            switch (tab) {
                case GENERAL:
                    pane = buildGeneralPane(this);
                    break;
                case ADMINISTRATION:
                    pane = buildAdministrationPane(this);
                    break;
            }
            pane.fillItems(0, 0, 8, 0, buildEmpty());
            addPane(1, 2, pane);
            fillItems(0, 0, 8, 3, buildPlaceholder());

        }}).forEach(this::registerGui);
    }

    protected Translation getTranslation() {
        return ItemMods.getTranslationConfig().subTranslation("gui.item");
    }

    protected GuiPane buildAdministrationPane(TranslatedChestGui gui) {
        GuiPane pane = new GuiPane(7, 1);
        var t = getTranslation();
        var asset = getAsset();
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("delete.title").lore("delete.description").build()) {{
            setClickAction(event -> {
                new MessageGui(t) {{
                    setActions(new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_BANNER).build()) {{
                        setClickAction(event -> {
                            Objects.requireNonNull(packObject.getPack()).unregisterItem(asset.getName());
                            new ModelsGui(packObject.getNamespace()).show((Player) event.getWhoClicked());
                        });
                    }}, new TranslatedGuiItem(new ItemStackBuilder(Material.RED_BANNER).build()) {{
                        setClickAction(event -> show((Player) event.getWhoClicked()));
                    }});
                }}.show((Player) event.getWhoClicked());
            });
        }});
        return pane;
    }

    protected GuiPane buildGeneralPane(TranslatedChestGui gui) {
        GuiPane pane = new GuiPane(7, 1);
        var t = getTranslation();
        var asset = getAsset();
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NAME_TAG).displayName("name.title").lore("name.description").build()) {{
            setRenderAction(gui -> setPlaceholders(asset.getName()));
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
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.PAPER).displayName("display-name.title").lore("display-name.description").build()) {{
            setRenderAction(gui -> setPlaceholders(asset.getDisplayName()));
            setClickAction(event -> {
                var p = (Player) event.getWhoClicked();
                hide(p);
                var request = new ChatRequest(p);
                p.sendMessage(t.getTranslation("display-name.message"));
                request.setSubmitAction(s -> {
                    try {
                        var ts = ChatColor.translateAlternateColorCodes('&', s);
                        asset.setDisplayName(ts);
                        show(p);
                        p.sendMessage(t.getTranslation("display-name.success", ts));
                    } catch (Exception e) {
                        p.sendMessage(t.getTranslation("display-name.failed"));
                        e.printStackTrace();
                    }
                });
            });
        }});
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder().build()) {{
            setRenderAction(gui -> {
                var prefix = "model." + (asset.getModel() == null ? "not-set" : "set") + ".";
                setItemStack(new ItemStackBuilder(Material.ARMOR_STAND).displayName(prefix + "title").lore(prefix + "description").build());
            });
            setClickAction(event -> {
                var p = (Player) event.getWhoClicked();
                var modelObject = asset.getModelObject();
                if (modelObject == null || event.getClick() == ClickType.RIGHT)
                    new ChoosePackGui(pack -> new ChooseModelGui(pack.getName(), modelAsset -> {
                        asset.setModelObject(new PackObject(pack.getName(), modelAsset.getName()));
                        packObject.save();
                        reloadAll();
                        show(p);
                    }).show(p)).show(p);
                else
                    switch (event.getClick()) {
                        case LEFT:
                            new ModelGui(modelObject).show(p);
                            break;
                        case DROP:
                            asset.setModelObject(null);
                            packObject.save();
                            reloadAll();
                    }
            });
        }});
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ENDER_CHEST).displayName("templates.title").lore("templates.description").build()));
        return pane;
    }

    protected GuiPane buildTabs(int index) {
        var pane = new GuiPane(9, 1);
        pane.addItem(buildPlaceholder());
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
            setClickAction(event -> new ItemsGui(packObject.getNamespace()).show((Player) event.getWhoClicked()));
        }});
        pane.addItem(buildPlaceholder());
        Arrays.stream(ItemTab.values()).map(tab -> new TranslatedGuiItem(new ItemStackBuilder(tab.getMaterial()).displayName(tab.name().toLowerCase()).setEnchanted(index == tab.ordinal()).build()) {{
            setClickAction(event -> setCurrent(tab.ordinal()));
        }}).forEach(pane::addItem);
        pane.fillItems(0, 0, 8, 0, buildPlaceholder());
        return pane;
    }

    protected GuiItem buildPlaceholder() {
        return new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName(" ").build());
    }

    protected GuiItem buildEmpty() {
        return new StaticItem(new ItemStackBuilder(Material.AIR).build());
    }

    public ItemAsset getAsset() {
        return packObject.getItem();
    }

    public enum ItemTab {
        GENERAL, ADMINISTRATION;

        public Material getMaterial() {
            switch (this) {
                case ADMINISTRATION:
                    return Material.COMMAND_BLOCK;
                case GENERAL:
                    return Material.ITEM_FRAME;
            }
            return Material.AIR;
        }
    }
}
