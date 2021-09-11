package dev.linwood.itemmods.pack;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
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

    /**
     * Construct a component based on the name and if it is translated
     * This only works for paper!
     *
     * @return A text component or a translatable component
     */
    public @NotNull Component constructComponent() {
        return translated ? Component.translatable(name) : Component.text(name);
    }

    public JsonObject save() {
        var jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("translated", translated);
        return jsonObject;
    }
}
