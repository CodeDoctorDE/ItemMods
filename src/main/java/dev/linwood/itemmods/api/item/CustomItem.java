package dev.linwood.itemmods.api.item;

import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.api.CustomElement;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItem implements CustomElement<ItemAsset> {
    protected static final NamespacedKey TYPE_KEY = new NamespacedKey(ItemMods.getPlugin(), "custom_item_type");
    protected static final NamespacedKey DATA_KEY = new NamespacedKey(ItemMods.getPlugin(), "custom_item_data");
    private final @NotNull ItemStack itemStack;

    public CustomItem(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public @Nullable ItemAsset getConfig() {
        var packObject = getPackObject();
        if (packObject == null)
            return null;
        return packObject.getItem();
    }

    @Override
    public void configure() {
        setString(new NamespacedKey(ItemMods.getPlugin(), "data"), "");
    }

    private @NotNull String getType() {
        return getString(TYPE_KEY);
    }

    public @NotNull String getData() {
        return getString(DATA_KEY);
    }

    public void setData(@NotNull String data) {
        setString(DATA_KEY, data);
    }

    private @NotNull String getString(@NotNull NamespacedKey key) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        return itemMeta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }

    private void setString(@NotNull NamespacedKey key, @NotNull String value) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        itemStack.setItemMeta(itemMeta);
    }

    public @Nullable PackObject getPackObject() {
        var type = getType();
        if (type.equals(""))
            return null;
        return new PackObject(type);
    }


    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }
}
