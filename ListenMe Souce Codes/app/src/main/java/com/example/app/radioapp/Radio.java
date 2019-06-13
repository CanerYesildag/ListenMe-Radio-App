package com.example.app.radioapp;

import java.io.Serializable;

public class Radio implements Serializable {
    private String url;
    private String channel;
    private boolean faved=false;
    private int id;

    public void setId(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public boolean isFaved() {
        return faved;
    }

    public void setFaved(boolean fav){
        this.faved=fav;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


}
