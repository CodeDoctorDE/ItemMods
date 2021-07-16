package com.github.codedoctorde.itemmods.pack;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class TextureAsset extends PackAsset {
    private byte[] data;

    public TextureAsset(URL url) throws IOException {

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
        data = output.toByteArray();
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void export(PackObject packObject, int packFormat, Path path) throws IOException {

    }

    @Override
    public JsonObject save(PackObject packObject) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", new String(data, StandardCharsets.UTF_8));
        return jsonObject;
    }

    @Override
    public void load(PackObject packObject, JsonObject jsonObject) {
        data = jsonObject.get("data").getAsString().getBytes(StandardCharsets.UTF_8);
    }
}
