package dev.linwood.itemmods.pack;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a name that can be translated.
 */
public class TranslatableName {
    private @NotNull String name = "";
    private boolean translated = false;

    /**
     * Create a new TranslatableName that is currently not translated.
     *
     * @param name The untranslated name.
     */
    public TranslatableName(@NotNull String name) {
        this.name = name;
    }

    /**
     * Load a TranslatableName from a JsonObject.
     *
     * @param jsonObject The JsonObject to load from.
     */
    public TranslatableName(JsonObject jsonObject) {
        if (jsonObject.has("name") && jsonObject.get("name").isJsonPrimitive())
            name = jsonObject.get("name").getAsString();
        if (jsonObject.has("translated") && jsonObject.get("translated").isJsonPrimitive())
            translated = jsonObject.get("translated").getAsBoolean();
    }

    /**
     * Get the name.
     *
     * @return The name.
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Set the name.
     *
     * @param name The name.
     */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    /**
     * Test if the name is currently translated.
     *
     * @return True if the name is currently translated.
     */
    public boolean isTranslated() {
        return translated;
    }

    /**
     * Set the name to be translated.
     *
     * @param translated True if the name should be translated. False if it should not be.
     */
    public void setTranslated(boolean translated) {
        this.translated = translated;
    }


    /**
     * Get the JsonObject representation of this TranslatableName.
     *
     * @return The JsonObject representation of this TranslatableName.
     */
    public JsonObject save() {
        var jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("translated", translated);
        return jsonObject;
    }

    /**
     * Get the string representation of this TranslatableName.
     *
     * @return The name of this TranslatableName.
     */
    @Override
    public String toString() {
        return name;
    }
}
