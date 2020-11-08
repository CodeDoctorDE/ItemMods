package com.github.codedoctorde.itemmods.config;

import java.util.Set;

/**
 * @author CodeDoctorDE
 */
public class ResourcePackConfig {
    private String referenceItem;
    private Set<String> customTextures;
    public ResourcePackConfig(){

    }

    public Set<String> getCustomTextures() {
        return customTextures;
    }

    public String getReferenceItem() {
        return referenceItem;
    }

    public void setReferenceItem(String referenceItem) {
        this.referenceItem = referenceItem;
    }
}
