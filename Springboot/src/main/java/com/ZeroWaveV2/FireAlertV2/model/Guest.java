package com.ZeroWaveV2.FireAlertV2.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "Guest")
public class Guest extends User {	
	@Column(length = 8)
	@ColumnDefault("'0'")
	private String userLevel;
	
	@CreationTimestamp
	@Column(name = "create_date", nullable = false, updatable = false)
	private Timestamp createDate;

	@UpdateTimestamp
	@Column(name = "modify_date")
	private Timestamp modifyDate;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FireReception> fireReceptions = new ArrayList<>();
	
	@PrePersist
    private void prePersist() {
        if (userLevel == null) {
            userLevel = "0";
        }
    }
	public Guest(String hp,Timestamp createDate, Timestamp modifyDate, String userLevel)	{
		super.hp=hp;
		this.createDate = createDate;
        this.modifyDate = modifyDate;
		this.userLevel = userLevel;
	}
}
