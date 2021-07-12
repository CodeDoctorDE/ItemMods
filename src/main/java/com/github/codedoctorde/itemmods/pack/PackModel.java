package com.github.codedoctorde.itemmods.pack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;

public class PackModel extends NamedPackObject {
    private String model;

    public PackModel(String url) throws IOException {
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

        model = response.toString();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    void export(ItemModsPack pack, Path path) throws IOException {

    }

    @Override
    void save(ItemModsPack pack, Path path) throws IOException {

    }

    @Override
    void load(ItemModsPack pack, Path path) throws IOException {

    }
}
