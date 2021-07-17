package com.github.codedoctorde.itemmods.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Pattern;

public abstract class NamedPackObject {
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static Pattern NAME_PATTERN = Pattern.compile("^[a-z_\\-]+(/+[a-z_\\-]+)*$");
    private String name;

    public NamedPackObject(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws UnsupportedOperationException {
        if (!NAME_PATTERN.matcher(name).matches())
            throw new UnsupportedOperationException();
        this.name = name;
    }
}
