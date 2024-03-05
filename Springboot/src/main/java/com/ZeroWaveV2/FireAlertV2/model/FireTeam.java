package com.ZeroWaveV2.FireAlertV2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "fire_team")
public class FireTeam {

    @Id
    @Column(name = "tc", length = 8, nullable = false)
    private String tc;

    @Column(name = "num", length = 8, nullable = false)
    private int num;
     
    @OneToMany(mappedBy = "fireTeam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FireStation> fireStation = new ArrayList<>();

    public FireTeam(String tc, int num) {
        this.tc = tc;
        this.num = num;
    }
}
