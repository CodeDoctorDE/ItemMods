package com.github.codedoctorde.itemmods.addon.templates.item;

import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.item.CustomItem;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.github.codedoctorde.itemmods.gui.block.choose.ChooseBlockConfigGui;
import com.github.codedoctorde.itemmods.gui.item.ItemGui;
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
    JsonObject templateTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("addon").getAsJsonObject("templates").getAsJsonObject("item").getAsJsonObject("block_set");

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {

    }

    @NotNull
    @Override
    public ItemStack getIcon(ItemConfig itemConfig) {
        return new ItemStackBuilder(templateTranslation.getAsJsonObject("icon")).build();
    }

    @NotNull
    @Override
    public ItemStack getMainIcon(ItemConfig itemConfig) {
        BlockConfig blockConfig = getBlock(itemConfig);
        if (blockConfig != null)
            return new ItemStackBuilder(templateTranslation.getAsJsonObject("main-icon").getAsJsonObject("has")).format(blockConfig.getNamespace(), blockConfig.getName()).build();
        else
            return new ItemStackBuilder(templateTranslation.getAsJsonObject("main-icon").getAsJsonObject("null")).build();
    }

    @Override
    public boolean isCompatible(ItemConfig itemConfig) {
        if (itemConfig.getItemStack() == null)
            return false;
        return itemConfig.getItemStack().getType().isBlock();
    }

    @Override
    public boolean openConfigGui(ItemConfig itemConfig, Player player) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, itemConfig);
        int itemIndex = ItemMods.getPlugin().getMainConfig().getItems().indexOf(itemConfig);
        new ChooseBlockConfigGui(blockConfig -> {
            data.setBlock(blockConfig.getIdentifier());
            data.save();
            new ItemGui(itemIndex).createGui().open(player);
        }).createGui(new ItemGui(itemIndex).createGui())[0].open(player);
        return true;
    }

    @NotNull
    @Override
    public String getName() {
        return templateTranslation.get("name").getAsString();
    }

    @Nullable
    public BlockConfig getBlock(CustomItem customItem) {
        return getBlock(customItem.getConfig());
    }

    @Nullable
    public BlockConfig getBlock(ItemConfig customItem) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, customItem);
        return ItemMods.getPlugin().getMainConfig().getBlock(data.getBlock());
    }

    public void setBlock(ItemConfig customItem, @Nullable BlockConfig blockConfig) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, customItem);
        if (blockConfig != null) {
            data.setBlock(blockConfig.getIdentifier());
        }
        else
            data.setBlock(null);
        data.save();
    }

    private class BlockSetTemplateData {
        private final ItemConfig itemConfig;
        private final BlockSetTemplate template;
        private final Gson gson = new Gson();
        private String block;

        BlockSetTemplateData(BlockSetTemplate template, ItemConfig itemConfig) {
            this.template = template;
            this.itemConfig = itemConfig;
            assert itemConfig.getTemplate() != null;
            JsonObject jsonObject = gson.fromJson(itemConfig.getTemplate().getData(), JsonObject.class);
            if (jsonObject.has("block") && jsonObject.get("block").isJsonPrimitive())
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

        public void save() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("block", block);
            assert itemConfig.getTemplate() != null;
            itemConfig.getTemplate().setData(gson.toJson(jsonObject));
            ItemMods.getPlugin().saveBaseConfig();
        }
    }
}
