package dev.linwood.itemmods.action.pack;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.GuiCollection;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.MessageGui;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.PacksAction;
import dev.linwood.itemmods.action.TranslationCommandAction;
import dev.linwood.itemmods.action.pack.raw.ModelAction;
import dev.linwood.itemmods.action.pack.raw.ModelsAction;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.TranslatableName;
import dev.linwood.itemmods.pack.asset.StaticItemAsset;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class ItemAction implements TranslationCommandAction {
    protected final @NotNull PackObject packObject;

    public ItemAction(@NotNull PackObject packObject) {
        this.packObject = packObject;
    }

    @Override
    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("item");
    }

    @Override
    public boolean showGui(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new GuiCollection();
        var asset = (StaticItemAsset) packObject.getItem();
        assert asset != null;
        var placeholder = new StaticItem(ItemStackBuilder.placeholder().build());
        Arrays.stream(ItemTab.values()).map(value -> new TranslatedChestGui(getTranslationNamespace(), 4) {{
            setPlaceholders(packObject.toString());
            fillItems(0, 0, 0, 3, placeholder);
            fillItems(8, 0, 8, 3, placeholder);
            addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
                setClickAction(event -> new ItemsAction(packObject.getNamespace()).showGui(event.getWhoClicked()));
            }});
            addItem(placeholder);
            Arrays.stream(ItemTab.values()).map(tab -> new TranslatedGuiItem(new ItemStackBuilder(tab.getMaterial()).displayName(tab.name().toLowerCase())
                    .setEnchanted(tab == value).build()) {{
                setClickAction(event -> gui.setCurrent(tab.ordinal()));
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
                            p.sendMessage(ItemAction.this.getTranslation("name.message"));
                            request.setSubmitAction(s -> {
                                try {
                                    asset.setName(s);
                                    packObject.save();
                                    p.sendMessage(ItemAction.this.getTranslation("name.success", s));
                                    var action = asset.generateAction(packObject.getNamespace());
                                    if (action != null)
                                        action.showGui(p);
                                } catch (Exception e) {
                                    p.sendMessage(ItemAction.this.getTranslation("name.failed"));
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
                            p.sendMessage(ItemAction.this.getTranslation("display.message"));
                            request.setSubmitAction(s -> {
                                var ts = ChatColor.translateAlternateColorCodes('&', s);
                                asset.setDisplayName(new TranslatableName(ts));
                                packObject.save();
                                show(p);
                                p.sendMessage(ItemAction.this.getTranslation("display.success", ts));
                            });
                        });
                    }});
                    addItem(new TranslatedGuiItem() {{
                        setRenderAction(gui -> {
                            var prefix = "model." + (asset.getModelObject() == null ? "not-set" : "set") + ".";
                            setItemStack(new ItemStackBuilder(Material.ARMOR_STAND).displayName(prefix + "title").lore(prefix + "description").build());
                            if (asset.getModelObject() != null) setPlaceholders(asset.getModelObject().toString());
                        });
                        setClickAction(event -> {
                            var p = (Player) event.getWhoClicked();
                            var modelObject = asset.getModelObject();
                            if (modelObject == null && event.getClick() == ClickType.SHIFT_LEFT)
                                if (packObject.getPack().getModel(packObject.getName()) != null)
                                    event.getWhoClicked().sendMessage(ItemAction.this.getTranslation("model.exist"));
                                else {
                                    pack.registerItem(new StaticItemAsset(pack.getName()));
                                    reloadAll();
                                }
                            if (modelObject == null || event.getClick() == ClickType.RIGHT)
                                new PacksAction().showChoose(event.getWhoClicked(), pack -> new ModelsAction(pack.getName()).showChoose(sender, modelAsset -> {
                                    asset.setModelObject(new PackObject(pack.getName(), modelAsset.getName()));
                                    packObject.save();
                                    reloadAll();
                                    show(p);
                                }), backEvent -> showGui(backEvent.getWhoClicked()));
                            else
                                switch (event.getClick()) {
                                    case LEFT:
                                        new ModelAction(modelObject).showGui(p);
                                        break;
                                    case DROP:
                                        asset.setModelObject(null);
                                        packObject.save();
                                        reloadAll();
                                }
                        });
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ENDER_CHEST).displayName("templates.title").lore("templates.description").build()) {{
                        setClickAction(event -> new TemplateAction(packObject, StaticItemAsset.class).showGui(sender, backEvent -> show((Player) backEvent.getWhoClicked())));
                    }});
                    break;
                case ADMINISTRATION:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("delete.title").lore("delete.description").build()) {{
                        setRenderAction(gui -> setPlaceholders(asset.getName()));
                        setClickAction(event -> new MessageGui(ItemMods.subTranslation("delete.gui")) {{
                            setPlaceholders(packObject.toString());
                            setActions(new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_BANNER).displayName("yes").build()) {{
                                setClickAction(event -> {
                                    Objects.requireNonNull(packObject.getPack()).unregisterItem(asset.getName());
                                    packObject.save();
                                    new ItemsAction(packObject.getNamespace()).showGui(event.getWhoClicked());
                                });
                            }}, new TranslatedGuiItem(new ItemStackBuilder(Material.RED_BANNER).displayName("no").build()) {{
                                setClickAction(event -> ItemAction.this.showGui(event.getWhoClicked()));
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

    public enum ItemTab {
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
