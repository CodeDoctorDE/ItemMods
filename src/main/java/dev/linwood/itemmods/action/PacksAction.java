package dev.linwood.itemmods.action;

import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.request.ChatRequest;
import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.item.GuiItem;
import dev.linwood.api.ui.template.gui.ListGui;
import dev.linwood.api.ui.template.gui.pane.list.VerticalListControls;
import dev.linwood.api.ui.template.item.TranslatedGuiItem;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.pack.PackAction;
import dev.linwood.itemmods.pack.ItemModsPack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class PacksAction extends CommandAction {
    @Override
    public Translation getTranslation() {
        return ItemMods.getTranslationConfig().subTranslation("packs").merge(ItemMods.getTranslationConfig().subTranslation("gui"));
    }

    @Override
    public boolean showGui(CommandSender... senders) {
        if (!(senders instanceof Player[])) {
            Arrays.stream(senders).forEach(sender -> sender.sendMessage(getTranslation().getTranslation("no-player")));
            return true;
        }
        var gui = new ListGui(getTranslation(), 4, (listGui) ->
                ItemMods.getPackManager().getPacks().stream().map(itemModsPack ->
                        new TranslatedGuiItem(
                                new ItemStackBuilder(itemModsPack.getIcon()).displayName("item").lore("action").build()) {{
                            setRenderAction(gui -> setPlaceholders(itemModsPack.getName()));
                            setClickAction(event -> openPack(event.getWhoClicked(), itemModsPack.getName()));
                        }}).toArray(GuiItem[]::new));
        var t = getTranslation();
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
        gui.show((Player[]) senders);
        return true;
    }

    public void openPack(CommandSender sender, String name) {
        var pack = ItemMods.getPackManager().getPack(name);
        assert pack != null;
        if (pack.isEditable())
            if (sender instanceof Player)
                new PackAction(pack.getName()).showGui(sender);
            else
                sender.sendMessage(getTranslation().getTranslation("no-player"));
        else
            sender.sendMessage(getTranslation().getTranslation("readonly"));
    }

    public void createPack(CommandSender sender) {
        createPack(sender, (Runnable) null);
    }

    public void createPack(CommandSender sender, @Nullable Runnable action) {
        var t = getTranslation();
        if (!(sender instanceof Player)) {
            sender.sendMessage(t.getTranslation("only-player"));
            return;
        }
        var p = (Player) sender;
        var request = new ChatRequest(p);
        p.sendMessage(t.getTranslation("create.message"));
        request.setSubmitAction(s -> {
            try {
                createPack(sender, s);
            } catch (UnsupportedOperationException e) {
                p.sendMessage(t.getTranslation("create.failed"));
            } finally {
                if (action != null)
                    action.run();
            }
        });
    }

    public void createPack(CommandSender sender, String name) throws UnsupportedOperationException {
        ItemMods.getPackManager().registerPack(new ItemModsPack(name, true));
        ItemMods.getPackManager().save(name);
        sender.sendMessage(getTranslation().getTranslation("create.success", name));
    }
}
