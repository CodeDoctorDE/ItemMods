package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.itemmods.ItemMods;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class PackObject {
    public static Pattern IDENTIFIER_PATTERN = Pattern.compile("^(?<namespace>^[a-z\\-]+):(?<name>[a-z_\\-]+(/+[a-z_\\-]+)*)$");
    private final String namespace, name;

    public PackObject(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    @Nullable
    public static PackObject fromIdentifier(String identifier) {
        var matcher = IDENTIFIER_PATTERN.matcher(identifier);
        if(!matcher.matches()) return null;
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
    public PackItem getItem() {
        var pack = getPack();
        if(pack == null)
            return null;
        return pack.getItem(name);
    }

    @Nullable
    public PackBlock getBlock() {
        var pack = getPack();
        if(pack == null)
            return null;
        return pack.getBlock(name);
    }
}
