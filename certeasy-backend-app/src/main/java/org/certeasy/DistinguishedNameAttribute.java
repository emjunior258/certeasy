package org.certeasy;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DistinguishedNameAttribute implements Serializable {

    private String key;

    @JsonProperty("display_name")
    private String displayName;
    private String description;

    @JsonProperty("repeatable")
    private boolean repeatable;

    @JsonProperty("max_items")
    private int maxItems;
    private boolean required;


    public DistinguishedNameAttribute(String key, String displayName, String description, boolean repetable, int maxItems, boolean required){
        this.key = key;
        this.displayName = displayName;
        this.description = description;
        this.repeatable = repetable;
        this.maxItems = maxItems;
        this.required = required;
    }


    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public boolean isRequired() {
        return required;
    }
}
