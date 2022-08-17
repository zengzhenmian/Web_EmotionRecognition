package com.zzm.entity;


import lombok.Data;

@Data
//视频数据库实体类
public class PictureUpload {
    private int id;
    private String pictureName;
    private String pictureUrl;
    private String pictureUUID;

    public PictureUpload(int id, String pictureName, String pictureUrl, String pictureUUID) {
        this.id = id;
        this.pictureName = pictureName;
        this.pictureUrl = pictureUrl;
        this.pictureUUID = pictureUUID;
    }

    public PictureUpload() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getpictureName() {
        return pictureName;
    }

    public void setpictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getpictureUrl() {
        return pictureUrl;
    }

    public void setpictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getpictureUUID() {
        return pictureUUID;
    }

    public void setpictureUUID(String pictureUUID) {
        this.pictureUUID = pictureUUID;
    }
}


