package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.linwood.itemmods.ItemMods;
import dev.linwood.itemmods.pack.NamedPackObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.custom.CustomTemplateData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PackAsset extends NamedPackObject {
    private final List<CustomTemplateData> customTemplates = new ArrayList<>();

    public PackAsset(@NotNull String name) {
        super(name);
    }

    public PackAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject.getName());
        jsonObject.getAsJsonArray("templates").forEach(o -> {
            var current = o.getAsJsonObject();
            try {
                registerCustomTemplate(new CustomTemplateData(new PackObject(current.get("object").getAsString()), current.get("data")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public @NotNull List<CustomTemplateData> getCustomTemplates() {
        return Collections.unmodifiableList(customTemplates);
    }

    public void registerCustomTemplate(CustomTemplateData data) {
        customTemplates.add(data);
    }

    public void registerCustomTemplate(PackObject template) {
        customTemplates.add(new CustomTemplateData(template));
    }

    public void unregisterCustomTemplate(int index) {
        customTemplates.remove(index);
    }

    public void unregisterCustomTemplate(PackObject packObject) {
        customTemplates.removeIf(customTemplateData -> customTemplateData.getObject().equals(packObject));
    }

    public JsonObject save(String namespace) {
        var jsonObject = new JsonObject();
        var customTemplatesArray = new JsonArray();
        customTemplates.stream().map(customTemplateData -> {
            JsonObject current = new JsonObject();
            current.add("data", customTemplateData.getData());
            current.addProperty("object", customTemplateData.getObject().toString());
            return current;
        }).forEach(customTemplatesArray::add);
        jsonObject.addProperty("version", ItemMods.FILE_VERSION);
        jsonObject.add("templates", customTemplatesArray);
        return jsonObject;
    }
}
