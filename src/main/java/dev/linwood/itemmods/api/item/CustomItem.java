package dev.linwood.itemmods.api.item;

import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.ItemAsset;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CustomItem {
    protected static final NamespacedKey TYPE_KEY = new NamespacedKey(ItemMods.getPlugin(), "custom_item_type");
    protected static final NamespacedKey DATA_KEY = new NamespacedKey(ItemMods.getPlugin(), "custom_item_data");
    private final @NotNull ItemStack itemStack;

    public CustomItem(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public @Nullable ItemAsset getConfig() {
        return getPackObject().getItem();
    }
    
    public @NotNull String getData() {
        return Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().getOrDefault(DATA_KEY, PersistentDataType.STRING, "");
    }

    public void setData(String data) {
        Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().set(DATA_KEY, PersistentDataType.STRING, "");
    }

    private @Nullable String getType() {
        return Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().get(TYPE_KEY, PersistentDataType.STRING);
    }

    public PackObject getPackObject() {
        var type = getType();
        if (type == null)
            return null;
        return new PackObject(type);
    }


    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }
}
