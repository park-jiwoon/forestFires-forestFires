package com.ZeroWaveV2.FireAlertV2.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ResultDto {
	private int dispatch;
    private Date dtime;
    private Date cdate;
    private String wtime;
    private int ff;
    private int ftruck;
    private int hc;
    private int fw;
    private int losses;
    private int lmoney;
    private int darea; 
    private int command;
}
