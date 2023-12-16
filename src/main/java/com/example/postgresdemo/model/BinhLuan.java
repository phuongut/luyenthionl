package com.example.postgresdemo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Service;

@Entity
@Table(name = "binhLuan")
@Service
public class BinhLuan implements Serializable {
	@Id
	String idBinhLuan;
	@Column(columnDefinition = "varchar(255)")
	String noiDung;
	@Column(columnDefinition = "varchar(255)")
	String anh;
	@Column(columnDefinition = "varchar(255)")
	String tieude;

	@ManyToOne
	@JoinColumn(name = "idNguoiDung")
	NguoiDung nguoiDung;

	public BinhLuan() {
	}

	public BinhLuan(String idBinhLuan, String noiDung, String anh, String tieude, NguoiDung nguoiDung) {
		this.idBinhLuan = idBinhLuan;
		this.noiDung = noiDung;
		this.anh = anh;
		this.tieude = tieude;
		this.nguoiDung = nguoiDung;
	}

	public String getIdBinhLuan() {
		return idBinhLuan;
	}

	public void setIdBinhLuan(String idBinhLuan) {
		this.idBinhLuan = idBinhLuan;
	}

	public String getNoiDung() {
		return noiDung;
	}

	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}

	public String getAnh() {
		return anh;
	}

	public void setAnh(String anh) {
		this.anh = anh;
	}

	public String getTieude() {
		return tieude;
	}

	public void setTieude(String tieude) {
		this.tieude = tieude;
	}

	public NguoiDung getNguoiDung() {
		return nguoiDung;
	}

	public void setNguoiDung(NguoiDung nguoiDung) {
		this.nguoiDung = nguoiDung;
	}

}
