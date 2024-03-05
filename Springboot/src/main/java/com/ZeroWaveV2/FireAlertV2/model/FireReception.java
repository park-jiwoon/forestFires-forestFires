package com.ZeroWaveV2.FireAlertV2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "Fire_Reception")
public class FireReception {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int num;
   
   @ManyToOne
    @JoinColumn(name = "hp", referencedColumnName = "hp")
   @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
      
   @Column(length=240)
   private String imgurl;
   
   @CreationTimestamp
   @Column(nullable = false, updatable = false)
   private Date adate;
   
   @Column(length=160)
   private String gps;
   
   @Column(length=16)
   @ColumnDefault("'진화 중'")
   private String progress;
      
   @OneToMany(mappedBy = "fireReception", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FireSituationRoom> fireSituationRoom = new ArrayList<>();
   
   // 240303 수정
   @Column(length=256)
   private String address; // 변환된 주소 정보 저장
   
   @PrePersist
    private void prePersist() {
        if (progress == null) {
           progress = "진화 중";
        }
    }
   
   FireReception(int num, User user, String imgurl, Date adate, String gps, String progress){
      this.num = num;
      this.user = user;
      this.imgurl = imgurl;
      this.adate = adate;
      this.gps = gps;
      this.progress = progress;
   }
}