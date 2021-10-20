package dev.linwood.itemmods.addon.simple.raw;

import com.google.gson.JsonObject;
import dev.linwood.itemmods.pack.PackObject;
import dev.linwood.itemmods.pack.asset.DefinedPackAsset;
import dev.linwood.itemmods.pack.asset.raw.RawAsset;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class SimpleRawAsset extends DefinedPackAsset implements RawAsset {

    protected final Map<String, byte[]> data = new HashMap<>();

    public SimpleRawAsset(@NotNull String name) {
        super(name);
    }

    public SimpleRawAsset(@NotNull String name, @NotNull String url) throws IOException {
        super(name);
        setDefaultData(url);
    }

    public SimpleRawAsset(@NotNull PackObject packObject, @NotNull JsonObject jsonObject) {
        super(packObject, jsonObject);
        jsonObject.getAsJsonObject("data").entrySet().forEach(entry -> data.put(entry.getKey(), entry.getValue().getAsString().getBytes(StandardCharsets.UTF_8)));
    }

    public void setDefaultData(byte[] bytes) {
        setData("default", bytes);
    }

    public void setDefaultData(@NotNull String url) throws IOException {
        setData("default", url);
    }

    public void removeVariation(String variation) {
        data.remove(variation);
    }

    @Override
    public @NotNull Set<String> getVariations() {
        return data.keySet();
    }

    @Override
    public byte[] getData(String variation) {
        return data.get(variation);
    }

    @Override
    public byte[] getDataOrDefault(String variation) {
        return data.getOrDefault(variation, getDefaultData());
    }

    public void setData(String variation, @NotNull String url) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream stream = new URL(url).openStream()) {
            byte[] buffer = new byte[4096];

            while (true) {
                int bytesRead = stream.read(buffer);
                if (bytesRead < 0) {
                    break;
                }
                output.write(buffer, 0, bytesRead);
            }
        }
        data.put(variation, output.toByteArray());
    }

    public void setData(String variation, byte[] bytes) {
        data.put(variation, bytes);
    }

    @Override
    public JsonObject save(String namespace) {
        var jsonObject = super.save(namespace);
        var dataJsonObject = new JsonObject();
        data.forEach((s, bytes) -> dataJsonObject.addProperty(s, new String(bytes, StandardCharsets.UTF_8)));
        jsonObject.add("data", dataJsonObject);
        return jsonObject;
    }

    public void export(String namespace, String variation, int packFormat, @NotNull Path path) throws IOException {

    }
}
