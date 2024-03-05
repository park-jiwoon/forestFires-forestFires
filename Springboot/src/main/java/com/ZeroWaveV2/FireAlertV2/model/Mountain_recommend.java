package com.ZeroWaveV2.FireAlertV2.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "mountain_recommend")
public class Mountain_recommend {
	
	@Id
	@Column(length=40)
	private String mt;
	
	@Column(name="address",length=16)
	private String address;
	
	@Column(length=8)
	private int height;
	
	@Column(length=16)
	private String season;
	
	@Column(length=16)
	private String mttime;
	
	@Column(length=240)
	private String mtpost;
	
	@Column(length=240)
	private String imgurl;
	
	@CreationTimestamp
	@Column(name = "create_date", nullable = false, updatable = false)
	private Timestamp createDate;
	
	@UpdateTimestamp
	@Column(name = "modify_date")
	private Timestamp modifyDate;
	
	public Mountain_recommend(String mt, String address, int height, String season, String mttime, String mtpost, String imgurl) {
		this.mt = mt;
		this.address = address;
		this.height = height;
		this.season= season;
		this.mttime = mttime;
		this.mtpost = mtpost;
		this.imgurl = imgurl;
	}
}