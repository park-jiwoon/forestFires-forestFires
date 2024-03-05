package com.ZeroWaveV2.FireAlertV2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "admin")
public class Admin {

	@Id
    @Column(name = "id", length = 16, nullable = false)
    private String id;
	
	@Column(length = 96, nullable = false)
    private String password;
	
	public Admin() {
    }

    public Admin(String id, String password) {
        this.id = id;
        this.password = password;
    }
}