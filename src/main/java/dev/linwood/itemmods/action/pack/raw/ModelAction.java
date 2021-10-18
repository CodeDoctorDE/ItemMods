package dev.linwood.itemmods.action.pack.raw;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.GuiCollection;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.MaterialListGui;
import dev.linwood.api.ui.template.gui.MessageGui;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.TranslationCommandAction;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import dev.linwood.itemmods.pack.asset.raw.StaticModelAsset;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class ModelAction implements TranslationCommandAction {
    private final PackObject packObject;

    public ModelAction(PackObject packObject) {
        this.packObject = packObject;
    }

    @Override
    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("raw.model");
    }

    @Override
    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new GuiCollection();
        var asset = (StaticModelAsset) packObject.getModel();
        assert asset != null;
        var placeholder = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName(" ").build());
        Arrays.stream(ModelTab.values()).map(value -> new TranslatedChestGui(getTranslationNamespace(), 4) {{
            setPlaceholders(packObject.toString());
            fillItems(0, 0, 0, 3, placeholder);
            fillItems(8, 0, 8, 3, placeholder);
            addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
                setClickAction(event -> new ModelsAction(packObject.getNamespace()).showGui(event.getWhoClicked()));
            }});
            addItem(placeholder);
            Arrays.stream(ModelTab.values()).map(tab -> new TranslatedGuiItem(new ItemStackBuilder(tab.getMaterial()).displayName(tab.name().toLowerCase())
                    .setEnchanted(tab == value).build()) {{
                setClickAction(event -> gui.setCurrent(tab.ordinal()));
            }}).forEach(gui::addItem);
            fillItems(0, 0, 8, 1, placeholder);
            switch (value) {
                case GENERAL:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NAME_TAG).displayName("name.title").lore("name.description").build()) {{
                        setRenderAction(gui -> setPlaceholders(asset.getName()));
                        setClickAction(event -> {
                            var p = (Player) event.getWhoClicked();
                            hide(p);
                            var request = new ChatRequest(p);
                            p.sendMessage(ModelAction.this.getTranslation("name.message"));
                            request.setSubmitAction(s -> {
                                try {
                                    asset.setName(s);
                                    packObject.save();
                                    p.sendMessage(ModelAction.this.getTranslation("name.success", s));
                                    new ModelAction(new PackObject(packObject.getNamespace(), s)).showGui(p);
                                } catch (Exception e) {
                                    p.sendMessage(ModelAction.this.getTranslation("name.failed"));
                                    e.printStackTrace();
                                }
                            });
                        });
                    }});
                    break;
                case APPEARANCE:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(asset.getFallbackTexture()).displayName("fallback.title").lore("fallback.description").build()) {{
                        setRenderAction(gui -> setPlaceholders(asset.getFallbackTexture().getKey().toString()));
                        setClickAction(event -> new MaterialListGui(ItemMods.subTranslation("materials", "gui"), material -> {
                            asset.setFallbackTexture(material);
                            packObject.save();
                            show((Player) event.getWhoClicked());
                        }).show((Player) event.getWhoClicked()));
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.IRON_INGOT).displayName("data.title").lore("data.description").build()) {{
                        setClickAction(event -> new DataAction(ModelAsset.class, packObject).showGui(sender));
                    }});
                    break;
                case ADMINISTRATION:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("delete.title").lore("delete.description").build()) {{
                        setRenderAction(gui -> setPlaceholders(asset.getName()));
                        setClickAction(event -> new MessageGui(ItemMods.subTranslation("delete.gui")) {{
                            setPlaceholders(packObject.toString());
                            setActions(new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_BANNER).displayName("yes").build()) {{
                                setClickAction(event -> {
                                    Objects.requireNonNull(packObject.getPack()).unregisterModel(asset.getName());
                                    new ModelsAction(packObject.getNamespace()).showGui(event.getWhoClicked());
                                });
                            }}, new TranslatedGuiItem(new ItemStackBuilder(Material.RED_BANNER).displayName("no").build()) {{
                                setClickAction(event -> showGui(event.getWhoClicked()));
                            }});
                        }}.show((Player) event.getWhoClicked()));
                    }});
                    break;
            }
            fillItems(0, 0, 8, 1, placeholder);
            fillItems(0, 3, 8, 3, placeholder);
        }}).forEach(gui::registerGui);
        gui.show((Player) sender);
        return true;
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
