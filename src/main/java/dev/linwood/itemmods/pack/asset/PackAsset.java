package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.DefinedNamedPackObject;
import org.jetbrains.annotations.NotNull;

public abstract class PackAsset extends DefinedNamedPackObject {

    public PackAsset(@NotNull String name) {
        super(name);
    }

    public PackAsset(@NotNull String name, @NotNull JsonObject jsonObject) {
        this(name);
    }

    public JsonObject save(String namespace) {
        var jsonObject = new JsonObject();
        jsonObject.addProperty("version", ItemMods.FILE_VERSION);
        return jsonObject;
    }
}
