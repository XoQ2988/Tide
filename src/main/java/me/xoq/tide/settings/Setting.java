package me.xoq.tide.settings;

public interface Setting <T>{
    String getName();
    String getTitle();
    String getKey();
    String getDescription();

    // getter
    T getValue();
    T getDefault();

    // setter
    void setValue(T value);
    void setKey(String s);

    default void reset() {
        setValue(getDefault());
    }

}