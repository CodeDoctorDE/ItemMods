package com.github.codedoctorde.itemmods.addon.templates.item;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.ChoosePackGui;
import com.github.codedoctorde.itemmods.gui.pack.block.ChooseBlockGui;
import com.github.codedoctorde.itemmods.gui.pack.template.TemplateGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplate;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplateData;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
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
    public boolean isCompatible(@NotNull PackObject packObject) {
        var item = packObject.getItem();
        if (item == null)
            return false;
        var model = item.getModel();
        return model != null;
    }

    @Override
    public boolean openConfigGui(PackObject packObject, CustomTemplateData data, Player player) {
        new ChoosePackGui(pack -> new ChooseBlockGui(pack.getName(), asset -> {
            var block = new PackObject(pack.getName(), asset.getName());
            setBlock(data, block);
            new TemplateGui(packObject).show(player);
        }).show(player)).show(player);
        return true;
    }

    @NotNull
    @Override
    public String getName() {
        return t.getTranslation("name");
    }

    public @Nullable PackObject getBlock(CustomTemplateData data) {
        if (data.getData() == null)
            return null;
        return PackObject.fromIdentifier(data.getData().getAsString());
    }

    public void setBlock(CustomTemplateData data, @Nullable PackObject packObject) {
        if (packObject == null)
            data.setData(JsonNull.INSTANCE);
        else
            data.setData(new JsonPrimitive(packObject.toString()));
    }
}
