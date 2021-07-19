package com.github.codedoctorde.itemmods.config;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CodeDoctorDE
 */
public class ResourcePackConfig {
    private final Map<String, Integer> resourceIdentifier = new HashMap<>();

    public ResourcePackConfig() {

    }

    public @NotNull Map<String, Integer> getResourceIdentifier() {
        return resourceIdentifier;
    }

    public void redefineResourceIdentifier() {
        resourceIdentifier.clear();
        /*ItemMods.getApi().getAddons().forEach(addons -> {
            Arrays.stream(addons.getStaticCustomItems()).forEach(customItem -> addResource(customItem.getIdentifier()));
            Arrays.stream(addons.getStaticCustomBlocks()).forEach(customBlock -> addResource(customBlock.getIdentifier()));
        });
        ItemMods.getMainConfig().getItems().forEach(itemAsset -> addResource(itemAsset.getIdentifier()));
        ItemMods.getMainConfig().getBlocks().forEach(blockConfig -> addResource(blockConfig.getIdentifier()));*/
    }

    public void addResource(String identifier) {
        if (resourceIdentifier.containsKey(identifier))
            return;
        int index = 0;
        while (!resourceIdentifier.containsValue(index))
            index++;
        resourceIdentifier.put(identifier, index);
    }
}
