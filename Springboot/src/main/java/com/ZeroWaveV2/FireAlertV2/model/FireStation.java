package com.ZeroWaveV2.FireAlertV2.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fire_station")
public class FireStation {
	
    @Id
    @Column(name = "fs", length = 16, nullable = false)
    private String fs;
    
    @ManyToOne
    @JoinColumn(name = "tc", referencedColumnName = "tc")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FireTeam fireTeam;

    @Column(length = 96, nullable = false)
    private String password;
    
    @Column(name = "fs_name", length = 80)
	private String fsName;
    
    @Column(name = "fs_add", length = 160)
	private String fsAdd;
    
    @Column(name = "fph", length = 16)
	private String fph;
    
    @CreationTimestamp
	@Column(name = "create_date", nullable = false, updatable = false)
	private Timestamp createDate;

	@UpdateTimestamp
	@Column(name = "modify_date")
	private Timestamp modifyDate;
    
    @OneToMany(mappedBy = "fireStation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FireSituationRoom> fireSituationRoom = new ArrayList<>();
    
    @Column(name = "latitude")
    private Double latitude; // 위도

    @Column(name = "longitude")
    private Double longitude; // 경도
    
    FireStation(String fs, FireTeam fireTeam, String password, String fsAdd){
    	this.fs = fs;
    	this.fireTeam = fireTeam;
    	this.password = password;
    	this.fsAdd = fsAdd;
    }
}