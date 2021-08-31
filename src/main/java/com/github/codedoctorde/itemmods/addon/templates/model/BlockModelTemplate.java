package com.github.codedoctorde.itemmods.addon.templates.model;

import com.github.codedoctorde.api.translations.Translation;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplate;
import com.github.codedoctorde.itemmods.pack.custom.CustomTemplateData;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlockModelTemplate extends CustomTemplate {
    private final Translation t = ItemMods.getTranslationConfig().subTranslation("addon.model.block");

    @Override
    public @NotNull String getName() {
        return "block_model_template";
    }

    @Override
    public @NotNull ItemStack getIcon(CustomTemplateData data) {
        return t.translate(new ItemStackBuilder(Material.GOLD_BLOCK).displayName("icon.title").lore("icon.description")).build();
    }

    @Override
    public @NotNull ItemStack getMainIcon() {
        return t.translate(new ItemStackBuilder(Material.GOLD_BLOCK).displayName("main_icon.title").lore("main_icon.description")).build();
    }

    @Override
    public boolean openConfigGui(CustomTemplateData data, Player player) {
        var element = data.getData();
        if (element == null)
            element = new JsonObject();
        var object = element.getAsJsonObject();

        return true;
    }
}
