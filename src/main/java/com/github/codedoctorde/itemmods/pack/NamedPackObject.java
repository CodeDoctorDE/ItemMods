package com.github.codedoctorde.itemmods.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public abstract class NamedPackObject {
    public static @NotNull Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static @NotNull Pattern NAME_PATTERN = Pattern.compile("^[a-z_\\-]+(/+[a-z_\\-]+)*$");
    private String name;

    public NamedPackObject(@NotNull String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) throws UnsupportedOperationException {
        System.out.println(name);
        if (!NAME_PATTERN.matcher(name).matches())
            throw new UnsupportedOperationException();
        this.name = name;
    }
}
