package com.github.codedoctorde.itemmods.addon.templates.item;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.api.item.CustomItem;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.github.codedoctorde.itemmods.gui.ItemGui;
import com.github.codedoctorde.itemmods.gui.choose.block.ChooseBlockConfigGui;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * @author CodeDoctorDE
 */
public class BlockSetTemplate implements CustomItemTemplate {
    JsonObject templateTranslation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("addon").getAsJsonObject("templates").getAsJsonObject("item").getAsJsonObject("block_set");

    @Override
    public ItemStack getIcon(ItemConfig itemConfig) {
        return new ItemStackBuilder(templateTranslation.getAsJsonObject("icon")).build();
    }

    @Override
    public ItemStack getMainIcon(ItemConfig itemConfig) {
        BlockConfig blockConfig = getBlock(itemConfig);
        if (blockConfig != null)
            return new ItemStackBuilder(templateTranslation.getAsJsonObject("main-icon").getAsJsonObject("has")).format(blockConfig.getTag()).build();
        else
            return new ItemStackBuilder(templateTranslation.getAsJsonObject("main-icon").getAsJsonObject("null")).build();
    }

    /**
     * Only compactible on block itemstacks
     *
     * @param itemConfig
     * @return
     */
    @Override
    public boolean isCompatible(ItemConfig itemConfig) {
        return itemConfig.getItemStack().getType().isBlock();
    }

    @Override
    public void openConfig(ItemConfig itemConfig, Player player) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, itemConfig);
        int itemIndex = Main.getPlugin().getMainConfig().getItems().indexOf(itemConfig);
        new ChooseBlockConfigGui(blockConfig -> {
            setBlock(itemConfig, blockConfig);
            data.save();
            new ItemGui(itemIndex).createGui().open(player);
        }).createGui(new ItemGui(itemIndex).createGui())[0].open(player);
    }

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
        return Main.getPlugin().getMainConfig().getBlock(data.getBlock());
    }

    public void setBlock(ItemConfig customItem, @Nullable BlockConfig blockConfig) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, customItem);
        if (blockConfig != null)
            data.setBlock(blockConfig.getTag());
        else
            data.setBlock(null);
    }

    private class BlockSetTemplateData {
        private final ItemConfig itemConfig;
        private final BlockSetTemplate template;
        private String block;

        private final Gson gson = new Gson();

        BlockSetTemplateData(BlockSetTemplate template, ItemConfig itemConfig) {
            this.template = template;
            this.itemConfig = itemConfig;
            JsonObject jsonObject = itemConfig.getTemplateConfig();
            if (jsonObject.has("block"))
                this.block = jsonObject.get("block").getAsString();
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
            itemConfig.setTemplateConfig(jsonObject);
            Main.getPlugin().saveBaseConfig();
        }
    }
}
