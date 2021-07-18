package com.github.codedoctorde.itemmods.pack.asset.raw;

import com.github.codedoctorde.itemmods.pack.PackObject;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;

public class ModelAsset extends RawAsset {
    private Material fallbackTexture = Material.STONE;
    private Integer staticModel = null;

    public ModelAsset(String name) {
        super(name);
    }

    public ModelAsset(String name, URL url) throws IOException {
        super(name, url);
    }

    public ModelAsset(PackObject packObject, JsonObject jsonObject) {
        super(packObject, jsonObject);
    }


    public boolean isStatic() {
        return staticModel != null;
    }

    public boolean hasFallbackTexture() {
        return fallbackTexture != null;
    }

    public Integer getStaticModel() {
        return staticModel;
    }

    public void setStaticModel(Integer staticModel) {
        this.staticModel = staticModel;
    }

    public @Nullable Material getFallbackTexture() {
        return fallbackTexture;
    }

    public void setFallbackTexture(@Nullable Material fallbackTexture) {
        this.fallbackTexture = fallbackTexture;
    }
}
