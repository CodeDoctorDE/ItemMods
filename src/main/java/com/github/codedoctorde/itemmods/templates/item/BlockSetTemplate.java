package com.github.codedoctorde.itemmods.templates.item;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.api.CustomBlock;
import com.github.codedoctorde.itemmods.api.CustomItem;
import com.github.codedoctorde.itemmods.api.CustomItemTemplate;
import com.github.codedoctorde.itemmods.config.BlockConfig;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.gitlab.codedoctorde.api.ui.Gui;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author CodeDoctorDE
 */
public class BlockSetTemplate implements CustomItemTemplate {
    JsonObject guiTranslation = Main.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("templates").getAsJsonObject("item").getAsJsonObject("block_set");

    @Override
    public ItemStack getIcon(ItemConfig itemConfig) {
        return new ItemStackBuilder(guiTranslation.getAsJsonObject("icon")).build();
    }

    @Override
    public ItemStack getMainIcon(ItemConfig itemConfig) {
        return new ItemStackBuilder(guiTranslation.getAsJsonObject("main-icon")).format().build();
    }

    @Override
    public void load(String data, Player player, CustomItem customItem) {
    }

    @Override
    public String save(CustomItem customItem) {
        return null;
    }

    @Override
    public Gui openConfig(BlockConfig blockConfig) {
        return null;
    }

    @Override
    public void onEnable(CustomBlock block) {

    }

    @Override
    public void onDisable(CustomBlock block) {

    }

    public BlockConfig getBlock(CustomItem customItem) {
        BlockSetTemplateData data = new BlockSetTemplateData(this, customItem);
        return Main.getPlugin().getMainConfig().getBlock(data.getBlock());
    }

    private class BlockSetTemplateData {
        private CustomItem customItem;
        private BlockSetTemplate template;
        private String block = "";

        private Gson gson = new Gson();

        BlockSetTemplateData(BlockSetTemplate template, CustomItem customItem) {
            this.template = template;
            this.customItem = customItem;
            if (customItem.getData().isEmpty()) {
                customItem.setData("{}");
            }
            JsonObject jsonObject = gson.fromJson(customItem.getData(), JsonObject.class);
            if (!jsonObject.get("data").isJsonNull())
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
            jsonObject.addProperty("data", block);
            customItem.setData(gson.toJson(jsonObject));
        }
    }
}
