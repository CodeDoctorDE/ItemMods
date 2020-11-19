package com.github.codedoctorde.itemmods.config;

import com.github.codedoctorde.itemmods.api.CustomTemplate;
import com.github.codedoctorde.itemmods.api.CustomTemplateData;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplateData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CodeDoctorDE
 */
public abstract class CustomConfig<T extends CustomTemplateData<?>> {
    private String name;
    private String namespace;
    private String displayName;
    private boolean pack = false;
    private T template = null;
    private final List<T> modifiers = new ArrayList<>();

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
        return namespace.replace("[^a-zA-Z0-9.\\-_]", "_");
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
        return getNamespace() + ":" + getName();
    }

    public boolean isPack() {
        return pack;
    }

    public void setPack(boolean pack) {
        this.pack = pack;
    }

    public @Nullable T getTemplate() {
        return template;
    }

    public void setTemplate(@Nullable T template) {
        this.template = template;
    }

    public List<T> getModifiers() {
        return modifiers;
    }
}
