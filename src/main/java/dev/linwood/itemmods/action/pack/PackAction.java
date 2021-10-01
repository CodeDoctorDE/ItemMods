package dev.linwood.itemmods.action.pack;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.GuiCollection;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.item.StaticItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.MessageGui;
import dev.linwood.api.ui.template.gui.TranslatedChestGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.PacksAction;
import dev.linwood.itemmods.action.TranslationCommandAction;
import dev.linwood.itemmods.action.pack.raw.ModelsGui;
import dev.linwood.itemmods.action.pack.raw.TexturesGui;
import dev.linwood.itemmods.pack.ItemModsPack;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

public class PackAction extends TranslationCommandAction {
    private final String name;

    public PackAction(String name) {
        this.name = name;
    }

    public static void showChoose(@NotNull Consumer<ItemModsPack> action, CommandSender sender) {
        showChoose(action, null, sender);
    }

    public static void showChoose(@NotNull Consumer<ItemModsPack> action, @Nullable Consumer<InventoryClickEvent> backAction, CommandSender sender) {
        var t = ItemMods.subTranslation("choose.pack", "gui");
        if (!(sender instanceof Player)) {
            sender.sendMessage(t.getTranslation("no-player"));
            return;
        }
        var gui = new ListGui(t, 4, (listGui) -> ItemMods.getPackManager().getPacks()
                .stream().filter(pack -> pack.getName().contains(listGui.getSearchText())).map(pack ->
                        new TranslatedGuiItem(
                                new ItemStackBuilder(pack.getIcon()).displayName("item").lore("action").build()) {{
                            setRenderAction(gui -> setPlaceholders(pack.getName()));
                            setClickAction(event -> action.accept(pack));
                        }}).toArray(GuiItem[]::new));
        var back = backAction;
        gui.setListControls(new VerticalListControls() {
            {
                setBackAction(back);
            }
        });
        gui.show((Player) sender);
    }

    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new GuiCollection();
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        if (!pack.isEditable()) {
            gui.registerItem(4, 2, new TranslatedGuiItem(new ItemStackBuilder(Material.STRUCTURE_VOID).displayName("readonly.title").lore("readonly.description").build()));
            return true;
        }
        var placeholder = new StaticItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).displayName(" ").build());
        Arrays.stream(PackTab.values()).map(value -> new TranslatedChestGui(getTranslationNamespace(), 4) {{
            setPlaceholders(name);
            fillItems(0, 0, 0, 3, placeholder);
            fillItems(8, 0, 8, 3, placeholder);
            addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.REDSTONE).displayName("back.title").lore("back.description").build()) {{
                setClickAction(event -> new PacksAction().showGui(event.getWhoClicked()));
            }});
            addItem(placeholder);
            Arrays.stream(PackTab.values()).map(tab -> new TranslatedGuiItem(new ItemStackBuilder(tab.getMaterial()).displayName(tab.name().toLowerCase())
                    .setEnchanted(tab == value).build()) {{
                setClickAction(event -> gui.setCurrent(tab.ordinal()));
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
                            togglePack(event.getWhoClicked());
                            reloadAll();
                        });
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.CHEST).displayName("export.title").lore("export.description").build()) {{
                        setClickAction(event -> exportPack(event.getWhoClicked()));
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.BARRIER).displayName("delete.title").lore("delete.description").build()) {{
                        setClickAction(event -> showDelete(event.getWhoClicked()));
                    }});
                    break;
                case GENERAL:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NAME_TAG).displayName("name.title").lore("name.description").build()) {{
                        setRenderAction((gui) -> setPlaceholders(pack.getName()));
                        setClickAction((event) -> {
                            var p = (Player) event.getWhoClicked();
                            hide(p);
                            showName(event.getWhoClicked(), () -> show((Player) event.getWhoClicked()));
                        });
                    }});
                    break;
                case CONTENTS:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.DIAMOND).displayName("items.title").lore("items.description").build()) {{
                        setClickAction(event -> showItems(event.getWhoClicked()));
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.GRASS_BLOCK).displayName("blocks.title").lore("blocks.description").build()) {{
                        setClickAction(event -> showBlocks(event.getWhoClicked()));
                    }});
                    break;
                case RAW:
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ITEM_FRAME).displayName("textures.title").lore("textures.description").build()) {{
                        setClickAction(event -> showTextures(event.getWhoClicked()));
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.ARMOR_STAND).displayName("models.title").lore("models.description").build()) {{
                        setClickAction(event -> showModels(event.getWhoClicked()));
                    }});
                    addItem(new TranslatedGuiItem(new ItemStackBuilder(Material.NOTE_BLOCK).displayName("sounds.title").lore("sounds.description").build()) {{
                        setClickAction(event -> showSounds(event.getWhoClicked()));
                    }});
                    break;
            }
            fillItems(0, 0, 8, 1, placeholder);
            fillItems(0, 3, 8, 3, placeholder);
        }}).forEach(gui::registerGui);
        gui.show((Player) sender);
        return true;
    }

    public void showSounds(CommandSender sender) {
        if (sender instanceof Player)
            sender.sendMessage(ItemMods.getTranslationConfig().getTranslation("coming-soon"));
        else
            sender.sendMessage(getTranslation("no-player"));
    }

    public void showModels(CommandSender sender) {
        if (sender instanceof Player)
            new ModelsGui(name).show((Player) sender);
        else
            sender.sendMessage(getTranslation("no-player"));
    }

    public void showTextures(CommandSender sender) {
        if (sender instanceof Player)
            new TexturesGui(name).show((Player) sender);
        else
            sender.sendMessage(getTranslation("no-player"));
    }

    public void showBlocks(CommandSender sender) {
        new BlocksAction(name).showGui(sender);
    }

    public void showItems(CommandSender sender) {
        new ItemsAction(name).showGui(sender);
    }

    public void showName(CommandSender sender) {
        showName(sender, null);
    }

    public void showName(CommandSender sender, @Nullable Runnable action) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return;
        }
        var request = new ChatRequest((Player) sender);
        sender.sendMessage(getTranslation("name.message"));
        request.setSubmitAction(s -> {
            setName(sender, s);
            if (action != null)
                action.run();
        });
    }

    public String getName() {
        return name;
    }

    public ItemModsPack getPack() {
        return ItemMods.getPackManager().getPack(name);
    }

    public void setName(CommandSender sender, String s) {
        getPack().setName(s);
        ItemMods.getPackManager().save(name);
        sender.sendMessage(getTranslation("name.success", s));
    }

    public void showDelete(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return;
        }
        var player = (Player) sender;
        new MessageGui(getTranslationNamespace().subTranslation("delete.gui")) {{
            setPlaceholders(name);
            setActions(new TranslatedGuiItem(new ItemStackBuilder(Material.GREEN_BANNER).displayName("yes").build()) {{
                setClickAction(event -> {
                    deletePack(player);
                    new PacksAction().showGui(player);
                });
            }}, new TranslatedGuiItem(new ItemStackBuilder(Material.RED_BANNER).displayName("no").build()) {{
                setClickAction(event -> showGui(player));
            }});
        }}.show(player);
    }

    public void deletePack(CommandSender sender) {
        ItemMods.getPackManager().deletePack(name);
    }

    public void exportPack(CommandSender sender) {
        sender.sendMessage(getTranslation("export.message", "plugins/ItemMods/exports/" + name));
        try {
            ItemMods.getPackManager().zip(name);
            sender.sendMessage(getTranslation("export.success", "plugins/ItemMods/exports/" + name));
        } catch (IOException e) {
            sender.sendMessage(getTranslation("export.failed"));
            e.printStackTrace();
        }
    }

    public void togglePack(CommandSender sender) {
        if (ItemMods.getPackManager().isActivated(name))
            ItemMods.getPackManager().deactivatePack(name);
        else
            ItemMods.getPackManager().activatePack(name);
    }

    @Override
    protected Translation getTranslationNamespace() {
        return ItemMods.subTranslation("pack", "gui");
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
