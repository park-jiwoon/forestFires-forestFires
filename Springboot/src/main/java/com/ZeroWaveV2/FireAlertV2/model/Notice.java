package com.ZeroWaveV2.FireAlertV2.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "notice")
public class Notice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int num;
	
	@Column(length=160)
	private String title;
	
	@Column(length=800)
	private String post;
	
	@Column(length=8)
	private Integer hit;
	
	@Column(length=240)
	private String imgurl;
	
	@CreationTimestamp
	@Column(name = "create_date", nullable = false, updatable = false)
	private Timestamp createDate;
	
	@UpdateTimestamp
	@Column(name = "modify_date")
	private Timestamp modifyDate;	
	
	@PrePersist
    private void prePersist() {
        if (hit == null) {
            hit = 0;
        }
    }
	
	public void increaseHit() {
	    this.hit += 1;
	}
}
