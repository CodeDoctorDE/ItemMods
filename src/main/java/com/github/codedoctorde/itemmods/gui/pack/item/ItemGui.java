package com.github.codedoctorde.itemmods.gui.pack.item;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.ui.GuiCollection;
import com.github.codedoctorde.api.ui.GuiPane;
import com.github.codedoctorde.api.ui.item.GuiItem;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.MaterialListGui;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.PackGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.ItemAsset;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ItemGui extends GuiCollection {
    protected final PackObject packObject;

    public ItemGui(PackObject packObject) {
        this.packObject = packObject;
        constructGuis();
    }

    protected void constructGuis() {
        var t = getTranslation();
        Arrays.stream(ItemTab.values()).map(itemTab -> new TranslatedChestGui(t, 4) {{
            addPane(buildTabs(itemTab.ordinal()));
            var pane = new GuiPane(7, 1);
            switch (itemTab) {
                case general:
                    pane = buildGeneralPane(this);
                    break;
                case item:
                    pane = buildItemPane(this);
                    break;
                case administration:
                    pane = buildAdministrationPane(this);
                    break;
            }
            addPane(1, 2, pane);
            fillItems(0, 0, 8, 3, buildPlaceholder());

        }}).forEach(this::registerGui);
    }

    protected Translation getTranslation() {
        return ItemMods.getTranslationConfig().subTranslation("gui.item");
    }

    protected GuiPane buildAdministrationPane(TranslatedChestGui gui) {
        GuiPane pane = new GuiPane(7, 1);
        pane.addItem(new StaticItem(new ItemStackBuilder(Material.BARRIER).displayName("delete.title").lore("delete.description").build()));
        return pane;
    }

    protected GuiPane buildGeneralPane(TranslatedChestGui gui) {
        GuiPane pane = new GuiPane(7, 1);
        pane.addItem(new StaticItem(new ItemStackBuilder(Material.NAME_TAG).displayName("name.title").lore("name.description").build()));
        pane.addItem(new StaticItem(new ItemStackBuilder(Material.PAPER).displayName("display-name.title").lore("display-name.description").build()));
        pane.addItem(new StaticItem(new ItemStackBuilder(Material.ENDER_CHEST).displayName("templates.title").lore("templates.description").build()));
        return pane;
    }

    protected GuiPane buildItemPane(TranslatedChestGui gui) {
        var pane = new GuiPane(7, 1);
        var asset = getAsset();
        var t = getTranslation();
        pane.addItem(new TranslatedGuiItem() {{
            setRenderAction(gui -> setItemStack(new ItemStackBuilder(asset.getModel().getFallbackTexture())
                    .displayName("material.title").lore("material.description").build()));
            setClickAction(event -> new MaterialListGui(ItemMods.getTranslationConfig().subTranslation("gui.materials"), material -> {
                asset.getModel().setFallbackTexture(material);
                packObject.save();
            }));
        }});
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder().build()) {{
            setRenderAction(gui -> {
                var prefix = asset.getModel().isCustom() ? "custom." : "preset.";
                setItemStack(new ItemStackBuilder(Material.ARMOR_STAND).displayName(prefix + "title")
                        .lore(prefix + "description").amount(asset.getModel().isStatic() ? asset.getModel().getStaticModel() : 1).build());
                if (!asset.getModel().isCustom())
                    setPlaceholders(asset.getModel().getPackObject().toString());
            });
            setClickAction(event -> {
                var p = (Player) event.getWhoClicked();
                switch (event.getClick()) {
                    case LEFT:
                        var request = new ChatRequest(p);
                        hide(p);
                        p.sendMessage(t.getTranslation("custom.message"));
                        request.setSubmitAction(s -> {
                            Integer model = null;
                            try {
                                model = Integer.parseInt(s);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            p.sendMessage(t.getTranslation("custom.success", model));
                            asset.getModel().setStaticModel(model);
                            gui.reloadAll();
                            show(p);
                        });
                        break;
                    case DROP:

                        break;
                }
            });
        }});
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder().build()) {{
            setRenderAction(gui -> {
                var prefix = asset.getModel().isStatic() ? "static." : "dynamic.";
                setItemStack(new ItemStackBuilder(Material.GOLD_NUGGET).displayName(prefix + "title")
                        .lore(prefix + "description").amount(asset.getModel().isStatic() ? asset.getModel().getStaticModel() : 1).build());
                if (asset.getModel().isStatic())
                    setPlaceholders(asset.getModel().getStaticModel());
            });
            setClickAction(event -> asset.getModel().setPackObject(packObject));
        }});
        return pane;
    }

    protected GuiPane buildTabs(int index) {
        var pane = new GuiPane(9, 1);
        pane.addItem(buildPlaceholder());
        pane.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
            setClickAction(event -> new PackGui(packObject.getNamespace()).show((Player) event.getWhoClicked()));
        }});
        pane.addItem(buildPlaceholder());
        Arrays.stream(ItemTab.values()).map(itemTab -> new TranslatedGuiItem(new ItemStackBuilder(itemTab.getMaterial()).displayName(itemTab.name()).setEnchanted(index == itemTab.ordinal()).build()) {{
            setClickAction(event -> setCurrent(itemTab.ordinal()));
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
        general, item, administration;

        public Material getMaterial() {
            switch (this) {
                case administration:
                    return Material.COMMAND_BLOCK;
                case general:
                    return Material.ITEM_FRAME;
                case item:
                    return Material.DIAMOND_SWORD;
            }
            return Material.AIR;
        }
    }
}
