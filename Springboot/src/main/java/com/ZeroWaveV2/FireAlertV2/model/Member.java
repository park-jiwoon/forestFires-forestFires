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
@Table(name = "member")
public class Member extends User {
	@Column(nullable = false, length = 96)
	private String password;

	@Column(name = "user_name", length = 120, nullable = false)
	private String userName;

	@CreationTimestamp
	@Column(name = "create_date", nullable = false, updatable = false)
	private Timestamp createDate;

	@UpdateTimestamp
	@Column(name = "modify_date")
	private Timestamp modifyDate;

	@Column(length = 8)
	@ColumnDefault("'1'")
	private String userLevel;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FireReception> fireReception = new ArrayList<>();
	
	@PrePersist
    private void prePersist() {
        if (userLevel == null) {
            userLevel = "1";
        }
    }
	
	public Member(String hp, String password, String userName) {
		super.hp = hp;
		this.password = password;
		this.userName = userName;
	}
	
	public Member(String hp,String password, String userName, Timestamp createDate, Timestamp modifyDate, String userLevel) {
		super.hp = hp;
		this.password = password;
		this.userName = userName;
		this.createDate = createDate;
        this.modifyDate = modifyDate;
		this.userLevel = userLevel;
	}

	public User orElseThrow() {
		// TODO Auto-generated method stub
		return null;
	}
}
