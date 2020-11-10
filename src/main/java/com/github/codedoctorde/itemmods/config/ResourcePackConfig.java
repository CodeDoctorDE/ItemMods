package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.ItemMods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author CodeDoctorDE
 */
public class ResourcePackConfig {
    private String referenceItem = "assets/minecraft/models/item/coal.json";
    private final Map<String, Integer> resourceIdentifier = new HashMap<>();
    private final Map<String, Integer> defaultResourceIdentifier = new HashMap<>();

    public ResourcePackConfig(){

    }

    public String getReferenceItem() {
        return referenceItem;
    }

    public void setReferenceItem(String referenceItem) {
        this.referenceItem = referenceItem;
    }

    public Map<String, Integer> getResourceIdentifier() {
        return resourceIdentifier;
    }

    public Map<String, Integer> getDefaultResourceIdentifier() {
        return defaultResourceIdentifier;
    }

    public void redefineResourceIdentifier(){
        resourceIdentifier.clear();
        resourceIdentifier.putAll(defaultResourceIdentifier);
        ItemMods.getPlugin().getApi().getAddons().forEach(addons -> {
            Arrays.stream(addons.getStaticCustomItems()).forEach(customItem -> addResource(customItem.getNamespace(), customItem.getName()));
            Arrays.stream(addons.getStaticCustomBlocks()).forEach(customBlock -> addResource(customBlock.getNamespace(), customBlock.getName()));
        });
        ItemMods.getPlugin().getMainConfig().getItems().forEach(itemConfig -> addResource(itemConfig.getNamespace(), itemConfig.getName()));
        ItemMods.getPlugin().getMainConfig().getBlocks().forEach(blockConfig -> addResource(blockConfig.getNamespace(), blockConfig.getName()));
    }

    public void addResource(String namespace, String name){
        int index = 0;
        while(!resourceIdentifier.containsValue(index))
            index++;
        resourceIdentifier.put(namespace.replace("[^a-zA-Z0-9.\\-_]", "_") + name, index);
    }
}
