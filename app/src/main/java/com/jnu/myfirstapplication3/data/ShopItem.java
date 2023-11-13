package com.jnu.myfirstapplication3.data;

import java.io.Serializable;

public class ShopItem implements Serializable {
    public int getImageId() {
        return imageResourceId;
    }

    private final int imageResourceId;

    public String getName() {
        return name;
    }


    private String name;

    public ShopItem(String name_, int imageResourceId_) {
        this.name = name_;
        this.imageResourceId = imageResourceId_;
    }

    public void setName(String name) {
        this.name = name;
    }
}
