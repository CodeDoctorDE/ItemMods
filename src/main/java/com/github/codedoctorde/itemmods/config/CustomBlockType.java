package com.github.codedoctorde.itemmods.config;

@Deprecated
public enum CustomBlockType {
    ARMOR_STAND,
    SPAWNER,
    HEAD;

    public CustomBlockType next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
