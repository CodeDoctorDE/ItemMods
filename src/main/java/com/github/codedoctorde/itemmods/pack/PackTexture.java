package com.github.codedoctorde.itemmods.pack;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PackTexture extends NamedPackObject {
    private byte[] data;

    public PackTexture(URL url) throws IOException {

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
    void export(ItemModsPack pack, Path path) {

    }

    @Override
    void save(ItemModsPack pack, Path path) throws IOException {
        var filePath = Path.of(path.toString(), getName());
        if (!Files.exists(filePath))
            Files.createFile(filePath);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", new String(data, StandardCharsets.UTF_8));
        Files.writeString(path, GSON.toJson(jsonObject));
    }

    @Override
    void load(ItemModsPack pack, Path path) throws IOException {
        JsonObject jsonObject = GSON.fromJson(Files.newBufferedReader(path), JsonObject.class);
        data = jsonObject.get("data").getAsString().getBytes(StandardCharsets.UTF_8);
    }
}
