package com.conoland.pokedex.models;

import androidx.annotation.NonNull;

public class Pokemon {

    private int number;
    private String name;
    private String url;
    private String weight;
    private String id;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    public int getNumber() {
        if (url != null){
            String[] urlPartes = url.split("/");
            return Integer.parseInt(urlPartes[urlPartes.length -1]);
        }

        return Integer.parseInt(id);
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
