package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.PackObject;
import org.jetbrains.annotations.NotNull;

public abstract class DefinedPackAsset extends DefinedNamedPackObject implements PackAsset {

    public DefinedPackAsset(@NotNull String name) {
        super(name);
    }

    public DefinedPackAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject.getName());
    }

    public JsonObject save(String namespace) {
        var jsonObject = new JsonObject();
        jsonObject.addProperty("version", ItemMods.FILE_VERSION);
        return jsonObject;
    }
}
