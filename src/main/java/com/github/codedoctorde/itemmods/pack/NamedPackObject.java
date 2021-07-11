package com.github.codedoctorde.itemmods.pack;

import java.util.regex.Pattern;

public abstract class NamedPackObject {
    private String name;
    Pattern NAME_PATTERN = Pattern.compile("^[a-z_\\-]+(/+[a-z_\\-]+)*$");

    public String getName() {
        return name;
    }

    public void setName(String name) throws UnsupportedOperationException {
        if(!NAME_PATTERN.matcher(name).matches())
            throw new UnsupportedOperationException();
        this.name = name;
    }
}
