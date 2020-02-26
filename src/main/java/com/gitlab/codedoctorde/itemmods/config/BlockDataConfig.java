package com.gitlab.codedoctorde.itemmods.config;

import com.gitlab.codedoctorde.itemmods.main.BlockTemplate;
import com.google.gson.JsonObject;
import org.bukkit.Location;

/**
 * @author CodeDoctorDE
 */
public class BlockDataConfig {
    private Location location;
    private BlockConfig blockConfig;
    private BlockTemplate blockTemplate;

    public JsonObject save() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("blocktemplate", blockTemplate.save(location, this));
        return jsonObject;
    }

    public void load(JsonObject jsonObject) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        blockTemplate = (BlockTemplate) Class.forName(jsonObject.get("type").getAsString()).newInstance();
        blockTemplate.load(jsonObject.get("blocktemplate"), location, this);
    }
}
