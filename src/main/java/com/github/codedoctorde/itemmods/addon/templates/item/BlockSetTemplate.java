package com.github.codedoctorde.itemmods.addon.templates.item;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.gui.pack.ChoosePackGui;
import com.github.codedoctorde.itemmods.gui.pack.block.ChooseBlockGui;
import com.github.codedoctorde.itemmods.gui.pack.item.ItemGui;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplate;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplateData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author CodeDoctorDE
 */
public class BlockSetTemplate extends CustomTemplate {
    private final Translation t = ItemMods.getTranslationConfig().subTranslation("addon.templates.item.block_set");

    @Override
    public @NotNull ItemStack createIcon(CustomTemplateData data) {
        return new ItemStackBuilder(t.getTranslation("icon")).build();
    }

    @Override
    public @NotNull ItemStack createMainIcon(@NotNull PackObject object) {
        var asset = object.getItem();
        if (asset != null)
            return new ItemStackBuilder(t.getTranslation("main-icon.has")).format(object.toString()).build();
        else
            return new ItemStackBuilder(t.getTranslation("main-icon.null")).build();
    }

    @Override
    public boolean isCompatible(@NotNull PackObject packObject) {
        var item = packObject.getItem();
        if(item == null)
            return false;
        var model = item.getModel();
        return model != null;
    }

    @Override
    public boolean openConfigGui(CustomTemplateData data, Player player) {
        new ChoosePackGui(pack -> {
            new ChooseBlockGui(pack.getName(), asset -> {
                var packObject = new PackObject(pack.getName(), asset.getName());
                setBlock(data, packObject);
                new ItemGui(packObject).show(player);
            });
        });
        return true;
    }

    @NotNull
    @Override
    public String getName() {
        return t.getTranslation("name");
    }

    @Nullable
    public PackObject getBlock(@NotNull CustomTemplateData data) {
        return new BlockSetTemplateData(data).getBlock();
    }
    public void setBlock(@NotNull CustomTemplateData data, @Nullable PackObject packObject) {
        var templateData = new BlockSetTemplateData(data);
        templateData.setBlock(packObject);
        templateData.save();
    }

    private static class BlockSetTemplateData {
        private static final Gson GSON = new Gson();
        private final CustomTemplateData data;
        private PackObject block;

        BlockSetTemplateData(@NotNull CustomTemplateData data) {
            this.data = data;
            JsonObject jsonObject = GSON.fromJson(data.getData(), JsonObject.class);
            if (jsonObject != null && jsonObject.has("block") && jsonObject.get("block").isJsonPrimitive())
                block = PackObject.fromIdentifier(jsonObject.get("block").getAsString());
        }

        public PackObject getBlock() {
            return block;
        }

        public void setBlock(PackObject block) {
            this.block = block;
        }

        void save() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("block", block.toString());
            data.setData(jsonObject);
            data.getObject().save();
        }
    }
}
