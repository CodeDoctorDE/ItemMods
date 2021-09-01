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
    private static final NamespacedKey TYPE_KEY = new NamespacedKey(ItemMods.getPlugin(), "custom_item_type");
    private static final NamespacedKey DATA_KEY = new NamespacedKey(ItemMods.getPlugin(), "custom_item_data");
    private final @NotNull ItemStack itemStack;

    public CustomItem(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public @Nullable ItemAsset getConfig() {
        try {
            var packObject = PackObject.fromIdentifier(Objects.requireNonNull(Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().get(TYPE_KEY, PersistentDataType.STRING)));
            if (packObject == null)
                return null;
            return packObject.getItem();
        } catch (Exception e) {
            return null;
        }
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


    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }
}
