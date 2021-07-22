package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.pack.PackObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CodeDoctorDE
 */
public class ResourcePackConfig {
    private final Map<String, Integer> identifiers = new HashMap<>();

    public ResourcePackConfig() {

    }

    public @NotNull Map<String, Integer> getIdentifiers() {
        return Collections.unmodifiableMap(identifiers);
    }

    public @Nullable Integer getIdentifier(String name) {
        return identifiers.get(name);
    }

    public @Nullable Integer getIdentifier(PackObject packObject) {
        return getIdentifier(packObject.toString());
    }

    public void setIdentifier(String name, @Nullable Integer integer) {
        identifiers.put(name, integer);
    }

    public void setIdentifier(PackObject packObject, @Nullable Integer integer) {
        setIdentifier(packObject.toString(), integer);
    }

    public void removeIdentifier(String name) {
        identifiers.remove(name);
    }

    public void removeIdentifier(PackObject packObject) {
        removeIdentifier(packObject.toString());
    }

    public void clearIdentifiers() {
        identifiers.clear();
    }
}
