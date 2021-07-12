package com.github.codedoctorde.itemmods.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

public abstract class NamedPackObject {
    public static final Pattern NAME_PATTERN = Pattern.compile("^[a-z_\\-]+(/+[a-z_\\-]+)*$");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) throws UnsupportedOperationException {
        if (!NAME_PATTERN.matcher(name).matches())
            throw new UnsupportedOperationException();
        this.name = name;
    }

    abstract void export(ItemModsPack pack, Path path) throws IOException;

    abstract void save(ItemModsPack pack, Path path) throws IOException;

    abstract void load(ItemModsPack pack, Path path) throws IOException;
}
