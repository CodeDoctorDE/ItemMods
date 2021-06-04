package com.github.codedoctorde.itemmods.addon.templates.item;

import com.github.codedoctorde.api.translations.Translation;
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
    private final Translation t = ItemMods.getTranslationConfig().subTranslation("addon.templates.item.block_set");

    @Override
    public void onLoad(Player player) {

    }

    @Override
    public void onUnload(Player player) {

    }

    @NotNull
    @Override
    public ItemStack createIcon(ItemConfig itemConfig) {
        return new ItemStackBuilder(t.getTranslation("icon")).build();
    }

    @NotNull
    @Override
    public ItemStack createMainIcon(ItemConfig itemConfig) {
        BlockConfig blockConfig = getBlock(itemConfig);
        if (blockConfig != null)
            return new ItemStackBuilder(t.getTranslation("main-icon.has")).format(blockConfig.getIdentifier()).build();
        else
            return new ItemStackBuilder(t.getTranslation("main-icon.null")).build();
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
        new ChooseBlockConfigGui(blockConfig -> {
            data.setBlock(blockConfig.getIdentifier());
            data.save();
            new ItemGui(itemConfig.getIdentifier()).show(player);
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
    public BlockConfig getBlock(ItemConfig customItem) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, customItem);
        return ItemMods.getMainConfig().getBlock(data.getBlock());
    }

    public void setBlock(ItemConfig customItem, @Nullable BlockConfig blockConfig) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, customItem);
        if (blockConfig != null) {
            data.setBlock(blockConfig.getIdentifier());
        } else
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

        public void save() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("block", block);
            assert itemConfig.getTemplate() != null;
            itemConfig.getTemplate().setData(gson.toJson(jsonObject));
            ItemMods.saveBaseConfig();
        }
    }
}
