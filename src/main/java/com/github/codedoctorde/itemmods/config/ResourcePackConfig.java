package com.github.codedoctorde.itemmods.config;

import java.util.Set;

/**
 * @author CodeDoctorDE
 */
public class ResourcePackConfig {
    private String referenceItem = "assets/minecraft/models/item/coal.json";

    public ResourcePackConfig(){

    }

    public String getReferenceItem() {
        return referenceItem;
    }

    public void setReferenceItem(String referenceItem) {
        this.referenceItem = referenceItem;
    }
}
