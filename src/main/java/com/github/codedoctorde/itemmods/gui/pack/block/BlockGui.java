package com.github.codedoctorde.itemmods.gui.pack.block;

import com.github.codedoctorde.api.request.ChatRequest;
import com.github.codedoctorde.api.ui.GuiCollection;
import com.github.codedoctorde.api.ui.item.StaticItem;
import com.github.codedoctorde.api.ui.template.gui.MessageGui;
import com.github.codedoctorde.api.ui.template.gui.TranslatedChestGui;
import com.github.codedoctorde.api.ui.template.item.TranslatedGuiItem;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.PacksGui;
import com.github.codedoctorde.itemmods.gui.pack.ChoosePackGui;
import com.github.codedoctorde.itemmods.gui.pack.raw.ModelsGui;
import com.github.codedoctorde.itemmods.gui.pack.raw.model.ChooseModelGui;
import com.github.codedoctorde.itemmods.gui.pack.raw.model.ModelGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class BlockGui extends GuiCollection {
    protected final @NotNull PackObject packObject;

    public BlockGui(@NotNull PackObject packObject) {
        this.packObject = packObject;
        var t = ItemMods.getTranslationConfig().subTranslation("gui.item");
        var asset = packObject.getItem();
        assert asset != null;
        var placeholder = new StaticItem(ItemStackBuilder.placeholder().build());
        var empty = new StaticItem();
        Arrays.stream(BlockTab.values()).map(value -> new TranslatedChestGui(t, 4) {{
            setPlaceholders(packObject.toString());
            fillItems(0, 0, 0, 3, placeholder);
            fillItems(8, 0, 8, 3, placeholder);
            addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
                setClickAction(event -> new PacksGui().show((Player) event.getWhoClicked()));
            }});
            addItem(placeholder);
            Arrays.stream(BlockTab.values()).map(tab -> new TranslatedGuiItem(new ItemStackBuilder(tab.getMaterial()).displayName(tab.name().toLowerCase())
                    .setEnchanted(tab == value).build()) {{
                setClickAction(event -> setCurrent(tab.ordinal()));
            }}).forEach(this::addItem);
            fillItems(0, 0, 8, 1, placeholder);
            switch (value) {
                case GENERAL:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NAME_TAG).displayName("name.title").lore("name.description").build()) {{
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
                                    p.sendMessage(t.getTranslation("name.success", s));
                                    new BlockGui(new PackObject(packObject.getNamespace(), s)).show(p);
                                } catch (Exception e) {
                                    p.sendMessage(t.getTranslation("name.failed"));
                                    e.printStackTrace();
                                }
                            });
                        });
                    }});
                    addItem(new TranslatedGuiItem() {{
                        setRenderAction(gui -> {
                            var prefix = "display-name." + (asset.getDisplayName() == null ? "not-set" : "set") + ".";
                            setItemStack(new ItemStackBuilder(Material.PAPER).displayName(prefix + "title").lore(prefix + "description").build());
                            if (asset.getDisplayName() != null) setPlaceholders(asset.getDisplayName());
                        });
                        setRenderAction(gui -> setPlaceholders(asset.getDisplayName()));
                        setClickAction(event -> {
                            var p = (Player) event.getWhoClicked();
                            hide(p);
                            var request = new ChatRequest(p);
                            p.sendMessage(t.getTranslation("display-name.message"));
                            request.setSubmitAction(s -> {
                                var ts = ChatColor.translateAlternateColorCodes('&', s);
                                asset.setDisplayName(ts);
                                show(p);
                                p.sendMessage(t.getTranslation("display-name.success", ts));
                            });
                        });
                    }});
                    addItem(new TranslatedGuiItem() {{
                        setRenderAction(gui -> {
                            var prefix = "localized-name." + (asset.getLocalizedName() == null ? "not-set" : "set") + ".";
                            setItemStack(new ItemStackBuilder(Material.BOOK).displayName(prefix + "title").lore(prefix + "description").build());
                            if (asset.getLocalizedName() != null) setPlaceholders(asset.getLocalizedName());
                        });
                        setClickAction(event -> {
                            var p = (Player) event.getWhoClicked();
                            hide(p);
                            var request = new ChatRequest(p);
                            p.sendMessage(t.getTranslation("localized-name.message"));
                            request.setSubmitAction(s -> {
                                asset.setDisplayName(s);
                                show(p);
                                p.sendMessage(t.getTranslation("localized-name.success", s));
                            });
                        });
                    }});
                    addItem(new TranslatedGuiItem() {{
                        setRenderAction(gui -> {
                            var prefix = "model." + (asset.getModel() == null ? "not-set" : "set") + ".";
                            setItemStack(new ItemStackBuilder(Material.ARMOR_STAND).displayName(prefix + "title").lore(prefix + "description").build());
                            if (asset.getModelObject() != null) setPlaceholders(asset.getModelObject().toString());
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
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ENDER_CHEST).displayName("templates.title").lore("templates.description").build()));
                    break;
                case ADMINISTRATION:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("delete.title").lore("delete.description").build()) {{
                        setClickAction(event -> new MessageGui(t.subTranslation("delete.gui")) {{
                            setActions(new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_BANNER).displayName("yes").build()) {{
                                setClickAction(event -> {
                                    Objects.requireNonNull(packObject.getPack()).unregisterItem(asset.getName());
                                    new ModelsGui(packObject.getNamespace()).show((Player) event.getWhoClicked());
                                });
                            }}, new TranslatedGuiItem(new ItemStackBuilder(Material.RED_BANNER).displayName("no").build()) {{
                                setClickAction(event -> BlockGui.this.show((Player) event.getWhoClicked()));
                            }});
                        }}.show((Player) event.getWhoClicked()));
                    }});
                    break;
            }
            fillItems(0, 0, 8, 1, placeholder);
            fillItems(0, 3, 8, 3, placeholder);
        }}).forEach(this::registerGui);
    }

    public enum BlockTab {
        GENERAL, ADMINISTRATION;

        public @NotNull Material getMaterial() {
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
