package dev.linwood.itemmods.pack;

import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.asset.BlockAsset;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import dev.linwood.itemmods.pack.asset.raw.TextureAsset;
import dev.linwood.itemmods.pack.custom.CustomTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class PackObject {
    public static @NotNull
    final Pattern IDENTIFIER_PATTERN = Pattern.compile("^(?<namespace>^[a-z\\-]+):(?<name>[a-z_\\-]+(/+[a-z_\\-]+)*)$");
    private final String namespace, name;

    public PackObject(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    @Nullable
    public static PackObject fromIdentifier(@NotNull String identifier) {
        var matcher = IDENTIFIER_PATTERN.matcher(identifier);
        if (!matcher.matches()) return null;
        return new PackObject(matcher.group("namespace"), matcher.group("name"));
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public ItemModsPack getPack() {
        return ItemMods.getPackManager().getPack(namespace);
    }

    @Nullable
    public PackAsset getAsset() {
        var item = getItem();
        if (item != null)
            return item;
        var block = getBlock();
        if (block != null)
            return block;
        var model = getModel();
        if (model != null)
            return model;
        return getTexture();
    }

    @Nullable
    public ItemAsset getItem() {
        var pack = getPack();
        if (pack == null)
            return null;
        return pack.getItem(name);
    }

    @Nullable
    public BlockAsset getBlock() {
        var pack = getPack();
        if (pack == null)
            return null;
        return pack.getBlock(name);
    }

    @Nullable
    public CustomTemplate getTemplate() {
        var pack = getPack();
        if (pack == null)
            return null;
        return pack.getTemplate(name);
    }

    @Nullable
    public ModelAsset getModel() {
        var pack = getPack();
        if (pack == null)
            return null;
        return pack.getModel(name);
    }

    @Nullable
    public TextureAsset getTexture() {
        var pack = getPack();
        if (pack == null)
            return null;
        return pack.getTexture(name);
    }

    public @Nullable Integer getCustomModel() {
        var asset = getModel();
        assert asset != null;
        if (asset.getStaticModel() != null)
            return asset.getStaticModel();
        return ItemMods.getMainConfig().getIdentifiers().getOrDefault(toString(), 0);
    }

    public void save() {
        ItemMods.getPackManager().save(namespace);
    }

    @Override
    public @NotNull String toString() {
        return namespace + ":" + name;
    }
}
