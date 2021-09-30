package dev.linwood.itemmods.action.pack.block;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.ui.GuiCollection;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.MessageGui;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.pack.BlocksGui;
import dev.linwood.itemmods.action.pack.PackAction;
import dev.linwood.itemmods.action.pack.raw.model.ChooseModelGui;
import dev.linwood.itemmods.action.pack.raw.model.ModelGui;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.TranslatableName;
import dev.linwood.itemmods.pack.asset.ItemAsset;
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
        var t = ItemMods.getTranslationConfig().subTranslation("item");
        var asset = packObject.getBlock();
        assert asset != null;
        var placeholder = new StaticItem(ItemStackBuilder.placeholder().build());
        Arrays.stream(BlockTab.values()).map(value -> new TranslatedChestGui(t, 4) {{
            setPlaceholders(packObject.toString());
            fillItems(0, 0, 0, 3, placeholder);
            fillItems(8, 0, 8, 3, placeholder);
            addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
                setClickAction(event -> new BlocksGui(packObject.getNamespace()).show((Player) event.getWhoClicked()));
            }});
            addItem(placeholder);
            Arrays.stream(BlockTab.values()).map(tab -> new TranslatedGuiItem(new ItemStackBuilder(tab.getMaterial()).displayName(tab.name().toLowerCase())
                    .setEnchanted(tab == value).build()) {{
                setClickAction(event -> setCurrent(tab.ordinal()));
            }}).forEach(this::addItem);
            fillItems(0, 0, 8, 1, placeholder);
            var pack = packObject.getPack();
            assert pack != null;
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
                            var prefix = "display." + (asset.getDisplayName() == null ? "not-set" : "set") + ".";
                            setItemStack(new ItemStackBuilder(Material.PAPER).displayName(prefix + "title").lore(prefix + "description").build());
                            if (asset.getDisplayName() != null) setPlaceholders(asset.getDisplayName());
                        });
                        setClickAction(event -> {
                            var p = (Player) event.getWhoClicked();
                            hide(p);
                            var request = new ChatRequest(p);
                            p.sendMessage(t.getTranslation("display.message"));
                            request.setSubmitAction(s -> {
                                var ts = ChatColor.translateAlternateColorCodes('&', s);
                                asset.setDisplayName(new TranslatableName(ts));
                                packObject.save();
                                show(p);
                                p.sendMessage(t.getTranslation("display.success", ts));
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
                            if (modelObject == null && event.getClick() == ClickType.SHIFT_LEFT)
                                if (packObject.getPack().getModel(packObject.getName()) != null)
                                    event.getWhoClicked().sendMessage(t.getTranslation("model.exist"));
                                else {
                                    pack.registerItem(new ItemAsset(pack.getName()));
                                    reloadAll();
                                }
                            if (modelObject == null || event.getClick() == ClickType.RIGHT)
                                PackAction.showChoose(pack -> new ChooseModelGui(pack.getName(), modelAsset -> {
                                    asset.setModelObject(new PackObject(pack.getName(), modelAsset.getName()));
                                    packObject.save();
                                    reloadAll();
                                    show(p);
                                }).show(p), backEvent -> BlockGui.this.show((Player) backEvent.getWhoClicked()), event.getWhoClicked());
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
                        setRenderAction(gui -> setPlaceholders(asset.getName()));
                        setClickAction(event -> new MessageGui(t.subTranslation("delete.gui")) {{
                            setPlaceholders(packObject.toString());
                            setActions(new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_BANNER).displayName("yes").build()) {{
                                setClickAction(event -> {
                                    Objects.requireNonNull(packObject.getPack()).unregisterItem(asset.getName());
                                    packObject.save();
                                    new BlocksGui(packObject.getNamespace()).show((Player) event.getWhoClicked());
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
