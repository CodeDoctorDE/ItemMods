package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.TranslatableName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class StaticCustomNamedAsset extends StaticPackAsset {
    private @Nullable TranslatableName displayName;

    public StaticCustomNamedAsset(@NotNull String name) {
        super(name);
    }

    public StaticCustomNamedAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
        if (jsonObject.has("display") && jsonObject.get("display").isJsonObject())
            displayName = new TranslatableName(jsonObject.getAsJsonObject("display"));
    }

    public @Nullable TranslatableName getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable TranslatableName displayName) {
        this.displayName = displayName;
    }

    public void removeDisplayName() {
        displayName = null;
    }

    @Override
    public JsonObject save(String namespace) {
        var jsonObject = super.save(namespace);
        jsonObject.add("display", displayName == null ? null : displayName.save());
        return jsonObject;
    }
}
