package com.didigen.models;


public class Addon {
    private int    id;

    //JSON用到
    public Addon() {

    }

    public Addon(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
