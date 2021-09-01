package dev.linwood.itemmods.addon.templates.model;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.gui.pack.ChoosePackGui;
import dev.linwood.itemmods.gui.pack.raw.texture.ChooseTextureGui;
import dev.linwood.itemmods.gui.pack.template.TemplateGui;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import dev.linwood.itemmods.pack.custom.CustomTemplate;
import dev.linwood.itemmods.pack.custom.CustomTemplateData;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemBlockModelTemplate extends CustomTemplate {
    private final Translation t = ItemMods.getTranslationConfig().subTranslation("addon.model.block");

    @Override
    public @NotNull String getName() {
        return "item_block";
    }

    @Override
    public @NotNull ItemStack getIcon(PackObject packObject, CustomTemplateData data) {
        return t.translate(new ItemStackBuilder(Material.GOLD_BLOCK).displayName("icon.title").lore("icon.description")).build();
    }

    @Override
    public @NotNull ItemStack getMainIcon() {
        return t.translate(new ItemStackBuilder(Material.GOLD_BLOCK).displayName("main_icon.title").lore("main_icon.description")).build();
    }

    @Override
    public boolean openConfigGui(PackObject packObject, CustomTemplateData data, Player player) {
        var element = data.getData();
        if (element == null)
            element = new JsonObject();
        var object = element.getAsJsonObject();

        new ChoosePackGui(pack -> new ChooseTextureGui(pack.getName(), asset -> {
            setModel(data, new PackObject(pack.getName(), asset.getName()));
            new TemplateGui(packObject).show(player);
        }).show(player)).show(player);

        return true;
    }

    public @Nullable PackObject getModel(CustomTemplateData data) {
        if (data.getData() == null)
            return null;
        return PackObject.fromIdentifier(data.getData().getAsString());
    }

    public void setModel(CustomTemplateData data, @Nullable PackObject packObject) {
        if (packObject == null)
            data.setData(JsonNull.INSTANCE);
        else
            data.setData(new JsonPrimitive(packObject.toString()));
    }

    @Override
    public boolean isCompatible(PackObject packObject, PackAsset packAsset) {
        return packAsset instanceof ModelAsset;
    }
}
