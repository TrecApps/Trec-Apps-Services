package com.trecapps.pictures.models;

import org.springframework.stereotype.Component;

@Component
public class PictureEntry {

    PictureModel model;

    String data;

    public PictureEntry() {
    }

    public PictureModel getModel() {
        return model;
    }

    public void setModel(PictureModel model) {
        this.model = model;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
