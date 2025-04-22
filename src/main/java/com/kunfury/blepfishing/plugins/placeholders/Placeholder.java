package com.kunfury.blepfishing.plugins.placeholders;

public abstract class Placeholder {

    public abstract String getName();

    public abstract String getValue(String[] args);

    public abstract String getDefault();

    public boolean Matches(String input){
        return getName().equalsIgnoreCase(input);
    }
}
