package com.github.codedoctorde.itemmods.pack;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;

public class PackModel extends PackAsset {
    private JsonObject model = new JsonObject();
    
    public PackModel(String name) {
        super(name);
    }
    public PackModel(String name, String url) throws IOException {
        super(name);
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        model = GSON.fromJson(response.toString(), JsonObject.class);
    }

    public JsonObject getModel() {
        return model;
    }

    public void setModel(JsonObject model) {
        this.model = model;
    }

    @Override
    public void export(PackObject packObject, int packFormat, Path path) throws IOException {

    }
}
