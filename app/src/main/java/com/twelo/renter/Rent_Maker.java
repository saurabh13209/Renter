package com.twelo.renter;


import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Rent_Maker {

    private Bitmap photo;
    private Integer room_num;
    private String name;
    private String cont_num;
    private String Add;
    private String trad;
    private String Year;

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
    public void setRoom_num(Integer room_num) {
        this.room_num = room_num;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setAdd(String add) {
        Add = add;
    }

    public void setTrad(String trad) {
        this.trad = trad;
    }

    public void setYear(String year) {
        Year = year;
    }

    public byte[] getPhoto() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public Integer getRoom_num() {
        return room_num;
    }

    public String getName() {
        return name;
    }

    public String getCont_num() {
        return cont_num;
    }

    public String getAdd() {
        return Add;
    }

    public String getTrad() {
        return trad;
    }

    public String getYear() {
        return Year;
    }

    public void setCont_num(String num) {
        cont_num = num;
    }
}
