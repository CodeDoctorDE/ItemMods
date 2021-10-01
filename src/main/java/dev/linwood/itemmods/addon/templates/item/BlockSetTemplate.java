package dev.linwood.itemmods.addon.templates.item;

import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.action.TranslationCommandAction;
import dev.linwood.itemmods.action.pack.PackAction;
import dev.linwood.itemmods.action.pack.block.ChooseBlockGui;
import dev.linwood.itemmods.action.pack.item.ItemGui;
import dev.linwood.itemmods.action.pack.template.TemplateGui;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
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
    private final Translation t = ItemMods.getTranslationConfig().subTranslation("addon.item.block").merge(ItemMods.getTranslationConfig().subTranslation("gui"));

    public BlockSetTemplate() {
        super("block");
    }

    @Override
    public @NotNull ItemStack getIcon(PackObject packObject, CustomData data, PackAsset asset) {
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
    public boolean isCompatible(PackObject packObject, PackAsset asset) {
        return asset instanceof ItemAsset;
    }

    @Override
    public @Nullable CommandAction generateAction(PackObject packObject, CustomData data, PackAsset asset) {
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
                PackAction.showChoose(pack -> new ChooseBlockGui(pack.getName(), asset -> {
                    var block = new PackObject(pack.getName(), asset.getName());
                    setBlock(data, block);
                    packObject.save();
                    new TemplateGui(packObject, asset, event -> new ItemGui(packObject).show(player)).show(player);
                }, event -> new ItemGui(packObject).show((Player) event.getWhoClicked())).show(player), inventoryClickEvent -> new ItemGui(packObject).show(player), player);
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
