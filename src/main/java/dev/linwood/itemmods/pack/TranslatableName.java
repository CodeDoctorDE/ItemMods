package dev.linwood.itemmods.pack;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class TranslatableName {
    private @NotNull String name = "";
    private boolean translated = false;

    public TranslatableName(@NotNull String name) {
        this.name = name;
    }

    public TranslatableName(JsonObject jsonObject) {
        if (jsonObject.has("name") && jsonObject.get("name").isJsonPrimitive())
            name = jsonObject.get("name").getAsString();
        if (jsonObject.has("translated") && jsonObject.get("translated").isJsonPrimitive())
            translated = jsonObject.get("translated").getAsBoolean();
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public boolean isTranslated() {
        return translated;
    }

    public void setTranslated(boolean translated) {
        this.translated = translated;
    }


    public JsonObject save() {
        var jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("translated", translated);
        return jsonObject;
    }
}
