package com.github.codedoctorde.itemmods.config;

import org.jetbrains.annotations.NotNull;

/**
 * @author CodeDoctorDE
 */
public abstract class CustomConfig {
    private String name;
    private String namespace;
    private String displayName;
    public CustomConfig(String namespace, String name){
        this.name = name;
        this.namespace = namespace;
        displayName = name;
    }
    public @NotNull String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIdentifier(){
        return namespace.replace("[^a-zA-Z0-9.\\-_]", "_") + name;
    }
}
