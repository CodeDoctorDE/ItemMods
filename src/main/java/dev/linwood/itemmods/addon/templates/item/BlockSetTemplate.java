package dev.linwood.itemmods.addon.templates.item;

import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.action.PacksAction;
import dev.linwood.itemmods.action.TranslationCommandAction;
import dev.linwood.itemmods.action.pack.BlocksAction;
import dev.linwood.itemmods.action.pack.ItemAction;
import dev.linwood.itemmods.action.pack.TemplateAction;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.StaticBlockAsset;
import dev.linwood.itemmods.pack.asset.StaticItemAsset;
import dev.linwood.itemmods.pack.asset.StaticPackAsset;
import dev.linwood.itemmods.pack.asset.TemplateReadyPackAsset;
import dev.linwood.itemmods.pack.custom.CustomData;
import dev.linwood.itemmods.pack.custom.CustomTemplate;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public class BlockSetTemplate extends CustomTemplate {
    private final Translation t = ItemMods.subTranslation("addon.item.block", "gui");

    public BlockSetTemplate() {
        super("block");
    }

    @Override
    public @NotNull ItemStack getIcon(PackObject packObject, CustomData data, TemplateReadyPackAsset asset) {
        var block = getBlock(data);
        return new ItemStackBuilder(Material.GRASS_BLOCK).displayName(t.getTranslation("title")).lore(
                block != null ?
                        t.getTranslation("action.has", block.toString()) : t.getTranslation("action.empty")).build();
    }

    @Override
    public @NotNull ItemStack getMainIcon() {
        return new ItemStackBuilder(Material.GRASS_BLOCK).displayName(t.getTranslation("title")).build();
    }

    @Override
    public boolean isCompatible(PackObject packObject, StaticPackAsset asset) {
        return asset instanceof StaticItemAsset;
    }

    @Override
    public @Nullable CommandAction generateAction(PackObject packObject, CustomData data, TemplateReadyPackAsset asset) {
        return new TranslationCommandAction() {
            @Override
            public Translation getTranslationNamespace() {
                return t;
            }

            @Override
            public boolean showGui(CommandSender sender) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(t.getTranslation("no-player"));
                    return true;
                }
                var player = (Player) sender;
                new PacksAction().showChoose(sender, pack -> new BlocksAction(pack.getName()).showChoose(player, asset -> {
                    var block = new PackObject(pack.getName(), asset.getName());
                    setBlock(data, block);
                    packObject.save();
                    new TemplateAction(packObject, StaticBlockAsset.class).showGui(sender, event -> new ItemAction(packObject).showGui(player));
                }, event -> new ItemAction(packObject).showGui(event.getWhoClicked())), event -> new ItemAction(packObject).showGui(player));
                return true;
            }
        };
    }


    public @Nullable PackObject getBlock(CustomData data) {
        if (data.getData() == null)
            return null;
        return new PackObject(data.getData().getAsString());
    }

    public void setBlock(CustomData data, @Nullable PackObject packObject) {
        if (packObject == null)
            data.setData(JsonNull.INSTANCE);
        else
            data.setData(new JsonPrimitive(packObject.toString()));
    }
}
