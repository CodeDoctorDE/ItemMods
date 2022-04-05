package dev.linwood.itemmods.pack;

import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.asset.CustomPackAsset;
import dev.linwood.itemmods.pack.asset.PackAsset;
import dev.linwood.itemmods.pack.asset.raw.ModelAsset;
import dev.linwood.itemmods.pack.custom.CustomGenerator;
import dev.linwood.itemmods.pack.custom.CustomTemplate;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * Represents a pack object.
 * A pack object represents an object in a pack.
 */
public class PackObject {
    public static @NotNull
    final Pattern IDENTIFIER_PATTERN = Pattern.compile("^(?<namespace>^[a-z\\-]+):(?<name>[a-z_\\-]+(/+[a-z_\\-]+)*)$");
    public static @NotNull
    final Pattern NAME_PATTERN = Pattern.compile("^[a-z_\\-]+(/+[a-z_\\-]+)*$");
    private final String namespace, name;

    /**
     * Create a pack object from a namespace and a name
     *
     * @param namespace The namespace of the pack
     * @param name      The name of the pack
     */
    public PackObject(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    /**
     * Create a pack object from an identifier. This identifier is a namespace:name string. The string should be lowercase and have no spaces.
     *
     * @param identifier The identifier to create the pack object from
     * @throws UnsupportedOperationException If the identifier is not a valid identifier
     */
    public PackObject(@NotNull String identifier) throws UnsupportedOperationException {
        var matcher = IDENTIFIER_PATTERN.matcher(identifier);
        if (!matcher.matches()) throw new UnsupportedOperationException();
        namespace = matcher.group("namespace");
        name = matcher.group("name");
    }

    /**
     * Create a new pack object from a namespaced key.
     *
     * @param key The namespaced key to create the pack object from
     */
    public PackObject(@NotNull NamespacedKey key) {
        namespace = key.getNamespace();
        name = key.getKey();
    }

    /**
     * Returns the namespaced key of the pack object
     *
     * @return The namespaced key of the pack object
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Returns the name of the pack object
     *
     * @return The name of the pack object
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the pack of the pack object
     *
     * @return The pack of the pack object
     */
    @Nullable
    public ItemModsPack getPack() {
        return PackManager.getInstance().getPack(namespace);
    }


    /**
     * Returns a block asset that can be found with this namespace and name
     *
     * @return A block asset or null if it does not exist
     */
    @Nullable
    public <T extends PackAsset> T getAsset(Class<T> type) {
        var pack = getPack();
        if (pack == null)
            return null;
        return pack.getAsset(type, name);
    }


    public CustomTemplate getTemplate() {
        var pack = getPack();
        if (pack == null)
            return null;
        return pack.getTemplate(name);
    }

    @Nullable
    public <T extends CustomPackAsset> CustomGenerator<T> getGenerator(Class<T> type) {
        var pack = getPack();
        if (pack == null)
            return null;
        return pack.getGenerator(type, name);
    }

    /**
     * Returns a custom model that can be found with this namespace and name.
     * It generates when the pack will get exported.
     * The method looks in the {@link dev.linwood.itemmods.config.MainConfig} for the custom model.
     *
     * @return A custom model or null if it does not exist
     */
    public @Nullable Integer getCustomModel() {
        var asset = getAsset(ModelAsset.class);
        assert asset != null;
        if (asset.getStaticModel() != null)
            return asset.getStaticModel();
        return ItemMods.getMainConfig().getIdentifiers().getOrDefault(toString(), 0);
    }

    /**
     * Saves the pack where the pack object is located
     */
    public void save() {
        PackManager.getInstance().save(namespace);
    }

    /**
     * This method returns the identifier of the pack object. It is a string in the format of namespace:name
     *
     * @return The string representation of the pack object
     */
    @Override
    public @NotNull String toString() {
        return namespace + ":" + name;
    }
}
