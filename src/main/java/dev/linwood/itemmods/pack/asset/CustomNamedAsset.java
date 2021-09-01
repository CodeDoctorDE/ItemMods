package dev.linwood.itemmods.pack.asset;

import dev.linwood.itemmods.pack.PackObject;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CustomNamedAsset extends PackAsset {
    private @Nullable String localizedName, displayName;

    public CustomNamedAsset(@NotNull String name) {
        super(name);
    }

    public CustomNamedAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
        if (jsonObject.has("localized-name"))
            localizedName = jsonObject.get("localized-name").getAsString();
        if (jsonObject.has("display"))
            displayName = jsonObject.get("display").getAsString();
    }

    public @Nullable String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(@Nullable String localizedName) {
        this.localizedName = localizedName;
    }

    public void removeLocalizedName() {
        localizedName = null;
    }

    public @Nullable String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    public void removeDisplayName() {
        displayName = null;
    }

    @Override
    public JsonObject save(String namespace) {
        var jsonObject = super.save(namespace);
        jsonObject.addProperty("display", displayName);
        jsonObject.addProperty("localized-name", localizedName);
        return jsonObject;
    }
}
