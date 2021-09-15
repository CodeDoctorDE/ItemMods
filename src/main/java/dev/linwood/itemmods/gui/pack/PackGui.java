package dev.linwood.itemmods.gui.pack;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.ui.GuiCollection;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.MessageGui;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.gui.PacksGui;
import dev.linwood.itemmods.gui.pack.raw.ModelsGui;
import dev.linwood.itemmods.gui.pack.raw.TexturesGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;

public class PackGui extends GuiCollection {
    public PackGui(String name) {
        var t = ItemMods.getTranslationConfig().subTranslation("pack");
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        if (!pack.isEditable()) {
            registerItem(4, 2, new TranslatedGuiItem(new ItemStackBuilder(Material.STRUCTURE_VOID).displayName("readonly.title").lore("readonly.description").build()));
            return;
        }
        var placeholder = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName(" ").build());
        Arrays.stream(PackTab.values()).map(value -> new TranslatedChestGui(t, 4) {{
            setPlaceholders(name);
            fillItems(0, 0, 0, 3, placeholder);
            fillItems(8, 0, 8, 3, placeholder);
            addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
                setClickAction(event -> new PacksGui().show((Player) event.getWhoClicked()));
            }});
            addItem(placeholder);
            Arrays.stream(PackTab.values()).map(tab -> new TranslatedGuiItem(new ItemStackBuilder(tab.getMaterial()).displayName(tab.name().toLowerCase())
                    .setEnchanted(tab == value).build()) {{
                setClickAction(event -> setCurrent(tab.ordinal()));
            }}).forEach(this::addItem);
            fillItems(0, 0, 8, 1, placeholder);
            switch (value) {
                case ADMINISTRATION:
                    addItem(new TranslatedGuiItem() {{
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
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.CHEST).displayName("export.title").lore("export.description").build()) {{
                        setClickAction(event -> {
                            event.getWhoClicked().sendMessage(t.getTranslation("export.message", "plugins/ItemMods/exports/" + pack.getName()));
                            try {
                                ItemMods.getPackManager().zip(pack.getName());
                                event.getWhoClicked().sendMessage(t.getTranslation("export.success", "plugins/ItemMods/exports/" + pack.getName()));
                            } catch (IOException e) {
                                event.getWhoClicked().sendMessage(t.getTranslation("export.failed"));
                                e.printStackTrace();
                            }
                        });
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("delete.title").lore("delete.description").build()) {{
                        setClickAction(event -> new MessageGui(t.subTranslation("delete.gui")) {{
                            setPlaceholders(pack.getName());
                            setActions(new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_BANNER).displayName("yes").build()) {{
                                setClickAction(event -> {
                                    ItemMods.getPackManager().deletePack(name);
                                    new PacksGui().show((Player) event.getWhoClicked());
                                });
                            }}, new TranslatedGuiItem(new ItemStackBuilder(Material.RED_BANNER).displayName("no").build()) {{
                                setClickAction(event -> PackGui.this.show((Player) event.getWhoClicked()));
                            }});
                        }}.show((Player) event.getWhoClicked()));
                    }});
                    break;
                case GENERAL:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NAME_TAG).displayName("name.title").lore("name.description").build()) {{
                        setRenderAction((gui) -> setPlaceholders(pack.getName()));
                        setClickAction((event) -> {
                            var p = (Player) event.getWhoClicked();
                            hide(p);
                            var request = new ChatRequest(p);
                            p.sendMessage(t.getTranslation("name.message"));
                            request.setSubmitAction(s -> {
                                pack.setName(s);
                                ItemMods.getPackManager().save(pack.getName());
                                p.sendMessage(t.getTranslation("name.success", s));
                                new PackGui(s).show(p);
                            });
                        });
                    }});
                    break;
                case CONTENTS:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND).displayName("items.title").lore("items.description").build()) {{
                        setClickAction(event -> new ItemsGui(name).show((Player) event.getWhoClicked()));
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.GRASS_BLOCK).displayName("blocks.title").lore("blocks.description").build()) {{
                        setClickAction(event -> new BlocksGui(name).show((Player) event.getWhoClicked()));
                    }});
                    break;
                case RAW:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ITEM_FRAME).displayName("textures.title").lore("textures.description").build()) {{
                        setClickAction(event -> new TexturesGui(name).show((Player) event.getWhoClicked()));
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ARMOR_STAND).displayName("models.title").lore("models.description").build()) {{
                        setClickAction(event -> new ModelsGui(name).show((Player) event.getWhoClicked()));
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NOTE_BLOCK).displayName("sounds.title").lore("sounds.description").build()) {{
                        setClickAction(event -> event.getWhoClicked().sendMessage(ItemMods.getTranslationConfig().getTranslation("coming-soon")));
                    }});
                    break;
            }
            fillItems(0, 0, 8, 1, placeholder);
            fillItems(0, 3, 8, 3, placeholder);
        }}).forEach(this::registerGui);
    }

    enum PackTab {
        GENERAL, CONTENTS, RAW, ADMINISTRATION;

        public @NotNull Material getMaterial() {
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
