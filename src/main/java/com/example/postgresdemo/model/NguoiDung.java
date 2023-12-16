package com.example.postgresdemo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "nguoiDung")
@Service
public class NguoiDung implements Serializable {
    @Id
    @Column(columnDefinition = "varchar(255)")
    String idND;

    @Column(columnDefinition = "varchar(255)")
    String hoTen;

    String soDienThoai;

    @Column(columnDefinition = "varchar(255)")
    String email;

    @OneToMany(mappedBy = "nguoiDung")
    List<KetQua> ketQua;

    public NguoiDung(String idND, String hoTen, String soDienThoai, String email, String matKhau, boolean isAdmin,
            String images) {
        this.idND = idND;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.matKhau = matKhau;
        this.isAdmin = isAdmin;
        this.images = images;
    }

    public String getIdND() {
        return idND;
    }

    public void setIdND(String idND) {
        this.idND = idND;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public NguoiDung() {
    }

    @Column(columnDefinition = "varchar(255)")
    String matKhau;
    boolean isAdmin = false;
    @Column(columnDefinition = "varchar(255)")
    String images;
    @Transient
    private MultipartFile imagesFile;

    public MultipartFile getImagesFile() {
        return imagesFile;
    }

    public NguoiDung(MultipartFile imagesFile) {
        this.imagesFile = imagesFile;
    }

    public void setImagesFile(MultipartFile imagesFile) {
        this.imagesFile = imagesFile;
    }

}
