package com.gitlab.codedoctorde.itemmods.api;

import com.gitlab.codedoctorde.itemmods.config.BlockConfig;
import com.gitlab.codedoctorde.itemmods.main.BlockTemplate;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

public class CustomBlock {
    private final Location location;
    private final BlockConfig config;
    private final ArmorStand armorStand;
    private BlockTemplate blockTemplate;

    public CustomBlock(BlockConfig config, Location location, ArmorStand armorStand) {
        this.location = location;
        this.config = config;
        this.armorStand = armorStand;
    }

    public BlockConfig getConfig() {
        return config;
    }

    public Location getLocation() {
        return location;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void breakBlock() {
        location.getBlock().breakNaturally();
    }

    public Block getBlock() {
        return location.getBlock();
    }

    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(jsonObject.get("type").getAsString(), blockTemplate.getClass().getCanonicalName());
        jsonObject.add("blocktemplate", blockTemplate.save(location, this));
        return jsonObject;
    }

    public void load(JsonObject jsonObject) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        blockTemplate = (BlockTemplate) Class.forName(jsonObject.get("type").getAsString()).newInstance();
        blockTemplate.load(jsonObject.get("blocktemplate"), location, this);
    }
}
