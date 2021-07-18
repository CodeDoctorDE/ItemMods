package com.github.codedoctorde.itemmods.addon.templates.item;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplate;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplateData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public @NotNull ItemStack createMainIcon(PackObject object) {
        var asset = object.getItem();
        if (asset != null)
            return new ItemStackBuilder(t.getTranslation("main-icon.has")).format(object.toString()).build();
        else
            return new ItemStackBuilder(t.getTranslation("main-icon.null")).build();
    }

    @Override
    public boolean isCompatible(PackObject packObject) {
        var item = packObject.getItem();
        assert item != null;
        var model = item.getModel();
        if (model == null)
            return false;
        return model.getFallbackTexture().isBlock();
    }

    @Override
    public boolean openConfigGui(CustomTemplateData data, Player player) {
        /*new ChooseBlockConfigGui(blockConfig -> {
            data.setBlock(blockConfig.getIdentifier());
            data.save();
            new ItemGui(itemAsset.getIdentifier()).show(player);
        });*/
        return true;
    }

    @NotNull
    @Override
    public String getName() {
        return t.getTranslation("name");
    }

    @Nullable
    public PackObject getBlock(CustomTemplateData data) {
        return PackObject.fromIdentifier(new BlockSetTemplateData(this, data).getBlock());
    }

    private static class BlockSetTemplateData {
        private static final Gson GSON = new Gson();
        private final BlockSetTemplate template;
        private String block;

        BlockSetTemplateData(BlockSetTemplate template, CustomTemplateData data) {
            this.template = template;
            JsonObject jsonObject = GSON.fromJson(data.getData(), JsonObject.class);
            if (jsonObject != null && jsonObject.has("block") && jsonObject.get("block").isJsonPrimitive())
                block = jsonObject.get("block").getAsString();
        }

        public BlockSetTemplate getTemplate() {
            return template;
        }

        public String getBlock() {
            return block;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        void save(CustomTemplateData data) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("block", block);
            data.setData(jsonObject);
            data.getObject().save();
        }
    }
}
