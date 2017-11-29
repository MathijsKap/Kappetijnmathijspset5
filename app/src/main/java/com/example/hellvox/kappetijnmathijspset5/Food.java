package com.example.hellvox.kappetijnmathijspset5;

public class Food {

    // Initialize variables
    private String name;
    private String url;
    private int price;
    private int menuid;

    // Construct the food object.
    public Food(String name, int price, int menuid, String url) {
        this.name = name;
        this.price = price;
        this.menuid = menuid;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMenuid() {
        return menuid;
    }

    public void setMenuid(int menuid) {
        this.menuid = menuid;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }


}
