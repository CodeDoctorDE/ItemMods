package com.github.codedoctorde.itemmods.addon.templates.item;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.item.CustomItem;
import com.github.codedoctorde.itemmods.gui.block.choose.ChooseBlockConfigGui;
import com.github.codedoctorde.itemmods.gui.item.ItemGui;
import com.github.codedoctorde.itemmods.pack.ItemAsset;
import com.github.codedoctorde.itemmods.pack.template.item.CustomItemTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public class BlockSetTemplate implements CustomItemTemplate {
    private final Translation t = ItemMods.getTranslationConfig().subTranslation("addon.templates.item.block_set");

    @Override
    public void onLoad(Player player) {

    }

    @Override
    public void onUnload(Player player) {

    }

    @NotNull
    @Override
    public ItemStack createIcon(ItemAsset itemAsset) {
        return new ItemStackBuilder(t.getTranslation("icon")).build();
    }

    @NotNull
    @Override
    public ItemStack createMainIcon(ItemAsset itemAsset) {
        BlockConfig blockConfig = getBlock(itemAsset);
        if (blockConfig != null)
            return new ItemStackBuilder(t.getTranslation("main-icon.has")).format(blockConfig.getIdentifier()).build();
        else
            return new ItemStackBuilder(t.getTranslation("main-icon.null")).build();
    }

    @Override
    public boolean isCompatible(ItemAsset itemAsset) {
        if (itemAsset.getItemStack() == null)
            return false;
        return itemAsset.getItemStack().getType().isBlock();
    }

    @Override
    public boolean openConfigGui(ItemAsset itemAsset, Player player) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, itemAsset);
        new ChooseBlockConfigGui(blockConfig -> {
            data.setBlock(blockConfig.getIdentifier());
            data.save();
            new ItemGui(itemAsset.getIdentifier()).show(player);
        });
        return true;
    }

    @NotNull
    @Override
    public String getName() {
        return t.getTranslation("name");
    }

    @Nullable
    public BlockConfig getBlock(CustomItem customItem) {
        return getBlock(customItem.getConfig());
    }

    @Nullable
    public BlockConfig getBlock(ItemAsset customItem) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, customItem);
        return ItemMods.getMainConfig().getBlock(data.getBlock());
    }

    public void setBlock(ItemAsset customItem, @Nullable BlockConfig blockConfig) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, customItem);
        if (blockConfig != null) {
            data.setBlock(blockConfig.getIdentifier());
        } else
            data.setBlock(null);
        data.save();
    }

    private class BlockSetTemplateData {
        private final ItemAsset itemAsset;
        private final BlockSetTemplate template;
        private final Gson gson = new Gson();
        private String block;

        BlockSetTemplateData(BlockSetTemplate template, ItemAsset itemAsset) {
            this.template = template;
            this.itemAsset = itemAsset;
            assert itemAsset.getTemplate() != null;
            JsonObject jsonObject = gson.fromJson(itemAsset.getTemplate().getData(), JsonObject.class);
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

        void save() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("block", block);
            assert itemAsset.getTemplate() != null;
            itemAsset.getTemplate().setData(gson.toJson(jsonObject));
            ItemMods.saveBaseConfig();
        }
    }
}
