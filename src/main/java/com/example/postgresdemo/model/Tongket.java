package com.example.postgresdemo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "Tongket")
public class Tongket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String diem;

    // @Column(name = "image_data", columnDefinition = "VARBINARY(MAX)", nullable =
    // true)
    // @Lob
    // @Column(name = "image_data", nullable = true)
    // private byte[] imageData;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "idNguoiDung")
    NguoiDung nguoiDung;

    String tenDe;

    public NguoiDung getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
    }

    public Tongket() {
    }

    public Tongket(byte[] imageData) {
        this.imageData = imageData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiem() {
        return diem;
    }

    public void setDiem(String diem) {
        this.diem = diem;
    }

    public Tongket(int id, String diem) {
        this.id = id;
        this.diem = diem;
    }

    public String getTenDe() {
        return tenDe;
    }

    public void setTenDe(String tenDe) {
        this.tenDe = tenDe;
    }

    public Tongket(String tenDe) {
        this.tenDe = tenDe;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

}
