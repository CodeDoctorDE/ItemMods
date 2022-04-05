package dev.linwood.itemmods.pack.asset;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.custom.TemplateData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CustomPackAsset extends CustomNamedAsset {
    private final List<TemplateData> templates = new ArrayList<>();


    public CustomPackAsset(@NotNull String name) {
        super(name);
    }

    public CustomPackAsset(@NotNull String name, @NotNull JsonObject jsonObject) {
        super(name, jsonObject);
        jsonObject.getAsJsonArray("templates").forEach(o -> {
            var current = o.getAsJsonObject();
            try {
                registerTemplate(new TemplateData(new PackObject(current.get("object").getAsString()), current.get("data")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public @NotNull List<TemplateData> getTemplates() {
        return Collections.unmodifiableList(templates);
    }

    public void registerTemplate(TemplateData data) {
        templates.add(data);
    }

    public void registerTemplate(PackObject template) {
        templates.add(new TemplateData(template));
    }

    public void unregisterTemplate(int index) {
        templates.remove(index);
    }

    public void unregisterTemplate(PackObject packObject) {
        templates.removeIf(customTemplateData -> customTemplateData.getObject().equals(packObject));
    }

    @Override
    public JsonObject save(String namespace) {
        var jsonObject = super.save(namespace);
        var templatesArray = new JsonArray();
        templates.stream().map(customTemplateData -> {
            JsonObject current = new JsonObject();
            current.add("data", customTemplateData.getData());
            current.addProperty("object", customTemplateData.getObject().toString());
            return current;
        }).forEach(templatesArray::add);
        jsonObject.add("templates", templatesArray);
        return jsonObject;
    }
}
