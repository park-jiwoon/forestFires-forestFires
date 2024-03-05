package com.ZeroWaveV2.FireAlertV2.dto;

import com.ZeroWaveV2.FireAlertV2.model.FireSituationRoom;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class FireReceptionDto {
    private int num;
    private String userHp;
    private String imgurl;
    private Date adate;
    private String gps;
    private String progress;
    private List<FireSituationRoom> fireSituationRoom;
    private String address;
    
    public FireReceptionDto(String imgurl, Date adate, String gps, String process) {
    	this.imgurl = imgurl;
    	this.adate = adate;
    	this.gps = gps;
    	this.progress = process;
    }

    // 모든 필드를 가지는 생성자
    public FireReceptionDto(int num, String userHp, String imgurl, Date adate, String gps, String progress, List<FireSituationRoom> fireSituationRoom) {
        this.num = num;
        this.userHp = userHp;
        this.imgurl = imgurl;
        this.adate = adate;
        this.gps = gps;
        this.progress = progress;
        this.fireSituationRoom = fireSituationRoom;
    }

    // 이미지와 날짜만을 인자로 받는 생성자
    public FireReceptionDto(String imgurl, Date adate) {
        this.imgurl = imgurl;
        this.adate = adate;
    }

    // progress 필드를 제외한 생성자
    public FireReceptionDto(String userHp, String imgurl, String gps) {
        this.userHp = userHp;
        this.imgurl = imgurl;
        this.gps = gps;
        // progress 필드는 이 생성자에서 초기화되지 않습니다.
    }
    
    public FireReceptionDto(String progress) {
    	this.progress = progress;
    }
}
