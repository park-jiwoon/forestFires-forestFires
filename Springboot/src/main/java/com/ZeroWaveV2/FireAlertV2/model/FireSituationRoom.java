package com.ZeroWaveV2.FireAlertV2.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "fire_situation_room")
public class FireSituationRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int command;
	
	@ManyToOne
    @JoinColumn(name = "fs", referencedColumnName = "fs")
	@OnDelete(action = OnDeleteAction.CASCADE)
    private FireStation fireStation;
	
	@ManyToOne
    @JoinColumn(name = "num", referencedColumnName = "num")
	@OnDelete(action = OnDeleteAction.CASCADE)
    private FireReception fireReception;
	
	@Column(length=16)
	private String state;
	
	@OneToMany(mappedBy = "fireSituationRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Result> result = new ArrayList<>();
	
	FireSituationRoom(int command, FireStation fireStation, FireReception fireReception, String state){
		this.command = command;
		this.fireStation = fireStation;
		this.fireReception = fireReception;
		this.state = state;
	}
}
