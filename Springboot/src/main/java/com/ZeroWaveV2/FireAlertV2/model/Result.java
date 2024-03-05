package com.ZeroWaveV2.FireAlertV2.model;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "result")
public class Result {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dispatch;
	
	@ManyToOne
    @JoinColumn(name = "command", referencedColumnName = "command")
	@OnDelete(action = OnDeleteAction.CASCADE)
    private FireSituationRoom fireSituationRoom;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date cdate;
	
	@Column(length=40)
	private String wtime;
	
	@Column(length=8)
	private int ff;
	
	@Column(length=8)
	private int ftruck;
	
	@Column(length=8)
	private int hc;
	
	@Column(length=16)
	private int fw;
	
	@Column(length=8)
	private int losses;
	
	@Column(length=16)
	private int lmoney;
	
	@Column(length=16)
	private int darea;
		
	@CreationTimestamp
	@Column(name = "create_date", nullable = false, updatable = false)
	private Timestamp createDate;
	
	@UpdateTimestamp
	@Column(name = "modify_date")
	private Timestamp modifyDate;
}
