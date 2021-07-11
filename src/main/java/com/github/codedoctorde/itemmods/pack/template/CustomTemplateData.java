package com.github.codedoctorde.itemmods.pack.template;

/**
 * @author CodeDoctorDE
 */
public abstract class CustomTemplateData<T extends CustomTemplate<?, ?>> {
    private final String name;
    private String data = "";

    public CustomTemplateData(String name) {
        this.name = name;
    }

    public CustomTemplateData(T template) {
        this(template.getClass().getName());
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public abstract T getInstance() throws ClassNotFoundException;

    public String getName() {
        return name;
    }
}
