package com.example.fetchrewardsapp;

/**
 * Item class used to do the data cleaning and sort operation.
 */
public class Item {

    // private fields
    private int Listid;
    private String id;
    private String name;

    /**
     *
     * @param listid item list id
     * @param id item id
     * @param name item name
     */
    public Item(int listid, String id, String name) {
        Listid = listid;
        this.id = id;
        this.name = name;
    }

    public int getListid() {
        return Listid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "id: " + this.getId() + ", name:" + this.getName();
    }
}
