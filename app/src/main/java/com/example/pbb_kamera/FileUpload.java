package com.example.pbb_kamera;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileUpload {

    @SerializedName("nama_kelompok")
    @Expose
    private String namaKelompok;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image")
    @Expose
    private String image;

    public String getNamaKelompok() {
        return namaKelompok;
    }

    public void setNamaKelompok(String namaKelompok) {
        this.namaKelompok = namaKelompok;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImage() {
        return image;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setImage(String image) {
        this.image = image;
    }

}