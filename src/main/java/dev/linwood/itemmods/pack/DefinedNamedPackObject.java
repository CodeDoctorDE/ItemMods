package dev.linwood.itemmods.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

public abstract class DefinedNamedPackObject {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected String name;

    public DefinedNamedPackObject(@NotNull String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) throws UnsupportedOperationException {
        if (!PackObject.NAME_PATTERN.matcher(name).matches())
            throw new UnsupportedOperationException();
        this.name = name;
    }
}
