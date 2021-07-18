package com.github.codedoctorde.itemmods.pack.asset.raw;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.github.codedoctorde.itemmods.pack.asset.PackAsset;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class RawAsset extends PackAsset {
    protected final Map<String, byte[]> data = new HashMap<>();

    public RawAsset(String name) {
        super(name);
        data.put("default", new byte[0]);
    }

    public RawAsset(String name, URL url) throws IOException {
        super(name);
        setDefaultTexture(url);
    }

    public RawAsset(PackObject packObject, JsonObject jsonObject) {
        super(packObject, jsonObject);
        jsonObject.getAsJsonObject("data").entrySet().forEach(entry -> data.put(entry.getKey(), entry.getValue().getAsString().getBytes(StandardCharsets.UTF_8)));
    }

    public byte[] getDefaultTexture() {
        return data.get("default");
    }

    public void setDefaultTexture(byte[] texture) {
        setTexture("default", texture);
    }

    public void setDefaultTexture(URL url) throws IOException {
        setTexture("default", url);
    }

    public void removeVariation(String bytes) {
        data.remove(bytes);
    }

    public Set<String> getVariations() {
        return data.keySet();
    }

    public byte[] getTexture(String variation) {
        return data.get(variation);
    }

    public byte[] getTextureOrDefault(String variation) {
        return data.getOrDefault(variation, getDefaultTexture());
    }

    public void setTexture(String variation, URL url) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream stream = url.openStream()) {
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

    public void setTexture(String variation, byte[] texture) {
        data.put(variation, texture);
    }

    @Override
    public JsonObject save(String namespace) {
        var jsonObject = super.save(namespace);
        var dataJsonObject = new JsonObject();
        data.forEach((s, bytes) -> dataJsonObject.addProperty(s, new String(bytes, StandardCharsets.UTF_8)));
        jsonObject.add("data", dataJsonObject);
        return jsonObject;
    }

    public void export(String name, String variation, int packFormat, Path path) throws IOException {

    }
}
