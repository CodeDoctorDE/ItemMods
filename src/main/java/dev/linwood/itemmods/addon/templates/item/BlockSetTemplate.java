package dev.linwood.itemmods.addon.templates.item;

import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.api.translations.Translation;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.gui.pack.ChoosePackGui;
import dev.linwood.itemmods.gui.pack.block.ChooseBlockGui;
import dev.linwood.itemmods.gui.pack.item.ItemGui;
import dev.linwood.itemmods.gui.pack.template.TemplateGui;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
import dev.linwood.itemmods.pack.custom.CustomTemplate;
import dev.linwood.itemmods.pack.custom.CustomTemplateData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public class BlockSetTemplate extends CustomTemplate {
    private final Translation t = ItemMods.getTranslationConfig().subTranslation("addon.item.block");

    @Override
    public @NotNull String getName() {
        return "block";
    }

    @Override
    public @NotNull ItemStack getIcon(PackObject packObject, CustomTemplateData data) {
        var block = getBlock(data);
        return new ItemStackBuilder(Material.GRASS_BLOCK).displayName(t.getTranslation("title")).lore(
                block != null ?
                        t.getTranslation("actions.has", block.toString()) : t.getTranslation("actions.empty")).build();
    }

    @Override
    public @NotNull ItemStack getMainIcon() {
        return new ItemStackBuilder(Material.GRASS_BLOCK).displayName(t.getTranslation("title")).build();
    }

    @Override
    public boolean isCompatible(PackObject packObject, PackAsset packAsset) {
        return packAsset instanceof ItemAsset;
    }

    @Override
    public boolean openConfigGui(PackObject packObject, CustomTemplateData data, Player player) {
        new ChoosePackGui(pack -> new ChooseBlockGui(pack.getName(), asset -> {
            var block = new PackObject(pack.getName(), asset.getName());
            setBlock(data, block);
            packObject.save();
            new TemplateGui(packObject, event -> new ItemGui(packObject).show((Player) event.getWhoClicked())).show(player);
        }, event -> new ItemGui(packObject).show((Player) event.getWhoClicked())).show(player)).show(player);
        return true;
    }


    public @Nullable PackObject getBlock(CustomTemplateData data) {
        if (data.getData() == null)
            return null;
        return new PackObject(data.getData().getAsString());
    }

    public void setBlock(CustomTemplateData data, @Nullable PackObject packObject) {
        if (packObject == null)
            data.setData(JsonNull.INSTANCE);
        else
            data.setData(new JsonPrimitive(packObject.toString()));
    }
}
