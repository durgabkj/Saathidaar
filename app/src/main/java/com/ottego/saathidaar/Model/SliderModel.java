package com.ottego.saathidaar.Model;

public class SliderModel {
    public SliderModel(String images_path) {
        this.images_path = images_path;
    }

    public String images_path;
    public String description;
    public String image_id;




    public String getImages_path() {
        return images_path;
    }

    public void setImages_path(String images_path) {
        this.images_path = images_path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }




}
