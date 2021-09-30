package dev.linwood.itemmods.pack.custom;

import com.google.gson.JsonElement;
import dev.linwood.itemmods.pack.PackObject;
import org.jetbrains.annotations.Nullable;

public class CustomData {
    private final PackObject object;
    private @Nullable JsonElement data;

    public CustomData(PackObject object, @Nullable JsonElement data) {
        this.object = object;
        this.data = data;
    }

    public CustomData(PackObject object) {
        this(object, null);
    }

    public @Nullable JsonElement getData() {
        return data;
    }

    public void setData(@Nullable JsonElement data) {
        this.data = data;
    }

    public PackObject getObject() {
        return object;
    }

    public CustomTemplate getTemplate() {
        return object.getTemplate();
    }

    public void save() {
        object.save();
    }
}