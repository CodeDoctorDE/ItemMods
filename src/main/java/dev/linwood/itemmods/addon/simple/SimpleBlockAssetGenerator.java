package dev.linwood.itemmods.addon.simple;

import com.google.gson.JsonObject;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.addon.actions.SimpleBlockGeneratorAction;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.custom.CustomAssetGenerator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SimpleBlockAssetGenerator implements CustomAssetGenerator<SimpleItemAsset> {
    @Override
    public @NotNull ItemStack getIcon(String namespace) {
        return new ItemStackBuilder(Material.GRASS_BLOCK).build();
    }

    @Override
    public CommandAction generateAction(PackObject packObject) {
        return new SimpleBlockGeneratorAction(packObject);
    }

    @Override
    public SimpleItemAsset loadAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        return new SimpleItemAsset(packObject, jsonObject);
    }

    @Override
    public String getName() {
        return "simple_block_asset_generator";
    }
}
