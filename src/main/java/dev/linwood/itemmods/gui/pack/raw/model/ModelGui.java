package dev.linwood.itemmods.gui.pack.raw.model;

import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.ui.GuiCollection;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.MaterialListGui;
import dev.linwood.api.ui.template.gui.MessageGui;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.gui.pack.raw.DataGui;
import dev.linwood.itemmods.gui.pack.raw.ModelsGui;
import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class ModelGui extends GuiCollection {
    public ModelGui(@NotNull PackObject packObject) {
        super();
        var t = ItemMods.getTranslationConfig().subTranslation("raw.model");
        var asset = packObject.getModel();
        assert asset != null;
        var placeholder = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName(" ").build());
        for (ModelTab value : ModelTab.values()) {
            var gui = new TranslatedChestGui(t, 4);
            gui.setPlaceholders(packObject.toString());
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
            gui.fillItems(0, 0, 8, 1, placeholder);
            switch (value) {
                case GENERAL:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NAME_TAG).displayName("name.title").lore("name.description").build()) {{
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
                                    new ModelGui(new PackObject(packObject.getNamespace(), s)).show(p);
                                } catch (Exception e) {
                                    p.sendMessage(t.getTranslation("name.failed"));
                                    e.printStackTrace();
                                }
                            });
                        });
                    }});
                    break;
                case APPEARANCE:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(asset.getFallbackTexture()).displayName("fallback.title").lore("fallback.description").build()) {{
                        setRenderAction(gui -> setPlaceholders(asset.getFallbackTexture().getKey().toString()));
                        setClickAction(event -> new MaterialListGui(ItemMods.getTranslationConfig().subTranslation("materials"), material -> {
                            asset.setFallbackTexture(material);
                            packObject.save();
                            show((Player) event.getWhoClicked());
                        }).show((Player) event.getWhoClicked()));
                    }});
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.IRON_INGOT).displayName("data.title").lore("data.description").build()) {{
                        setClickAction(event -> new DataGui(packObject.getNamespace(), asset, () -> {
                            packObject.save();
                            show((Player) event.getWhoClicked());
                        }, (variation) -> {
                            var itemStack = new ItemStack(Material.WRITTEN_BOOK);
                            var itemMeta = (BookMeta) itemStack.getItemMeta();
                            assert itemMeta != null;
                            itemMeta.setTitle(variation);
                            itemMeta.setAuthor("§6ItemMods");
                            itemMeta.addPage(new String(asset.getData(variation)));
                            itemStack.setItemMeta(itemMeta);
                            ((Player)event.getWhoClicked()).openBook(itemStack);
                        }).show((Player) event.getWhoClicked()));
                    }});
                    break;
                case ADMINISTRATION:
                    gui.addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("delete.title").lore("delete.description").build()) {{
                        setRenderAction(gui -> setPlaceholders(asset.getName()));
                        setClickAction(event -> new MessageGui(t.subTranslation("delete.gui")) {{
                            setActions(new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_BANNER).displayName("yes").build()) {{
                                setClickAction(event -> {
                                    Objects.requireNonNull(packObject.getPack()).unregisterModel(asset.getName());
                                    new ModelsGui(packObject.getNamespace()).show((Player) event.getWhoClicked());
                                });
                            }}, new TranslatedGuiItem(new ItemStackBuilder(Material.RED_BANNER).displayName("no").build()) {{
                                setClickAction(event -> ModelGui.this.show((Player) event.getWhoClicked()));
                            }});
                        }}.show((Player) event.getWhoClicked()));
                    }});
                    break;
            }
            gui.fillItems(0, 0, 8, 1, placeholder);
            gui.fillItems(0, 3, 8, 3, placeholder);
            registerGui(gui);
        }
    }

    enum ModelTab {
        GENERAL, APPEARANCE, ADMINISTRATION;

        public @NotNull Material getMaterial() {
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
