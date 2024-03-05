package com.ZeroWaveV2.FireAlertV2.dto;

import java.util.Date;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class FireSituationRoomDto {
   int command;   
   String state;
   private String imgurl;
    private String gps;
    private Date adate;
    private String progress;
    //240303 수정
    private String address;
}