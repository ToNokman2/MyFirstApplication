package com.jnu.myfirstapplication3.data;

public class ShopItem {
    public int getImageId() {
        return imageResourceId;
    }

    private final int imageResourceId;

    public String getName() {
        return name;
    }


    private final String name;

    public ShopItem(String name_, int imageResourceId_) {
        this.name = name_;
        this.imageResourceId = imageResourceId_;
    }
}
