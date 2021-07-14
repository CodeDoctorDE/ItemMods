package com.github.codedoctorde.itemmods.pack;

import com.github.codedoctorde.itemmods.pack.PackAsset;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CustomTemplate<C extends PackAsset> {
    public CustomTemplate(JsonObject data){

    }

    public abstract String getName();

    public abstract @NotNull ItemStack createIcon(C config);

    public abstract @NotNull ItemStack createMainIcon(C config);

    public boolean isCompatible(C config) {
        return true;
    }

    public boolean openConfigGui(C config, Player player) {
        return false;
    }

    public JsonObject saveData() {
        var jsonObject = new JsonObject();
        jsonObject.addProperty("class", getClass().getName());
        return jsonObject;
    }
}
