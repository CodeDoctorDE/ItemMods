package com.github.codedoctorde.itemmods.pack;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class PackSound extends PackAsset {
    private byte[] data;

    public PackSound(String name) {
        super(name);
        data = new byte[0];
    }

    public PackSound(String name, URL url) throws IOException {
        super(name);
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
        JsonObject jsonObject = super.save(packObject);
        jsonObject.addProperty("data", new String(data, StandardCharsets.UTF_8));
        return jsonObject;
    }

    @Override
    public void load(PackObject packObject, JsonObject jsonObject) {
        super.load(packObject, jsonObject);
        data = jsonObject.get("data").getAsString().getBytes(StandardCharsets.UTF_8);
    }
}
