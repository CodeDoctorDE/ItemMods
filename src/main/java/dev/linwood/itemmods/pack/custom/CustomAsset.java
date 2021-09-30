package dev.linwood.itemmods.pack.custom;

public abstract class CustomAsset {
    private final String name;

    public CustomAsset(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
