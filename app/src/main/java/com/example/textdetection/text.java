package com.example.textdetection;

public class text {
    private int id ;
    private String text;
    private byte[] image;

    public text(int id, String text, byte[] image) {
        this.id = id;
        this.text = text;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
