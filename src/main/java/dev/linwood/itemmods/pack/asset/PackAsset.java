package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.DefinedNamedPackObject;
import dev.linwood.itemmods.pack.PackObject;
import org.jetbrains.annotations.NotNull;

public abstract class PackAsset extends DefinedNamedPackObject {

    public PackAsset(@NotNull String name) {
        super(name);
    }

    public PackAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        this(packObject.getName());
    }

    public JsonObject save(String namespace) {
        var jsonObject = new JsonObject();
        jsonObject.addProperty("version", ItemMods.FILE_VERSION);
        return jsonObject;
    }
}
