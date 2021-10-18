package dev.linwood.itemmods.action;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.ItemModsPack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PacksAction implements TranslationCommandAction {
    @Override

    public Translation getTranslationNamespace() {
        return ItemMods.subTranslation("packs", "gui");
    }

    @Override
    public boolean showGui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("no-player"));
            return true;
        }
        var gui = new ListGui(getTranslationNamespace(), 4, (listGui) ->
                ItemMods.getPackManager().getPacks().stream().map(itemModsPack ->
                        new TranslatedGuiItem(
                                new ItemStackBuilder(itemModsPack.getIcon()).displayName("item").lore("action").build()) {{
                            setRenderAction(gui -> setPlaceholders(itemModsPack.getName()));
                            setClickAction(event -> openPack(event.getWhoClicked(), itemModsPack.getName()));
                        }}).toArray(GuiItem[]::new));
        gui.setListControls(new VerticalListControls() {{
            setBackAction(event -> new MainAction().showGui(event.getWhoClicked()));
            setCreateAction(event -> {
                gui.hide((Player) event.getWhoClicked());
                createPack(event.getWhoClicked(), () -> {
                    gui.rebuild();
                    gui.show((Player) event.getWhoClicked());
                });
            });
        }});
        gui.show((Player) sender);
        return true;
    }

    public String getTranslation(String key, Object... placeholders) {
        return getTranslationNamespace().getTranslation(key, placeholders);
    }

    public void openPack(CommandSender sender, String name) {
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        var action = pack.generateAction();
        if (pack.isEditable() && action != null)
            if (sender instanceof Player)
                action.showGui(sender);
            else
                sender.sendMessage(getTranslation("no-player"));
        else
            sender.sendMessage(getTranslation("readonly"));
    }

    public void createPack(CommandSender sender) {
        createPack(sender, (Runnable) null);
    }

    public void createPack(CommandSender sender, @Nullable Runnable action) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getTranslation("only-player"));
            return;
        }
        var p = (Player) sender;
        var request = new ChatRequest(p);
        p.sendMessage(getTranslation("create.message"));
        request.setSubmitAction(s -> {
            try {
                createPack(sender, s);
            } catch (UnsupportedOperationException e) {
                p.sendMessage(getTranslation("create.failed"));
            } finally {
                if (action != null)
                    action.run();
            }
        });
    }

    public void createPack(CommandSender sender, String name) throws UnsupportedOperationException {
        ItemMods.getPackManager().registerPack(new ItemModsPack(name, true));
        ItemMods.getPackManager().save(name);
        sender.sendMessage(getTranslation("create.success", name));
    }

    public void showChoose(CommandSender sender, @NotNull Consumer<ItemModsPack> action) {
        showChoose(sender, action, null);
    }

    public void showChoose(CommandSender sender, @NotNull Consumer<ItemModsPack> action, @Nullable Consumer<InventoryClickEvent> backAction) {
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
}
