package dev.linwood.itemmods.pack.asset.raw;

import com.google.gson.JsonObject;
import dev.linwood.api.item.ItemStackBuilder;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticSoundAsset extends StaticRawAsset implements SoundAsset {
    public StaticSoundAsset(@NotNull String name) {
        super(name);
    }

    public StaticSoundAsset(@NotNull String name, @NotNull String url) throws IOException {
        super(name, url);
    }

    public StaticSoundAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
    }

    @Override
    public void export(String namespace, String variation, int packFormat, @NotNull Path path) throws IOException {
        var currentPath = Paths.get(path.toString(), "assets", namespace, "sounds", getName() + ".ogg");
        Files.createDirectories(currentPath.getParent());
        Files.write(currentPath, getDataOrDefault(variation));
    }

    @Override
    public @NotNull ItemStack getIcon(String namespace) {
        return new ItemStackBuilder(Material.NOTE_BLOCK).displayName(ItemMods.getTranslation("gui.item", name)).build();
    }
}
