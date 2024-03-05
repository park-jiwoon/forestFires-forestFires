package com.ZeroWaveV2.FireAlertV2.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

import com.ZeroWaveV2.FireAlertV2.model.Notice;

@RequiredArgsConstructor
@Data
public class NoticeDto {
    private int num;
    private String title;
    private String post;
    private int hit;
    private String imgurl;
    private Timestamp createDate;
    private Timestamp modifyDate;
    
    public NoticeDto(int num, String title, Timestamp createDate) {
        this.num = num;
        this.title = title;
        this.createDate = createDate;
    }
    
    public NoticeDto(int num, String title, String post, int hit, String imgurl, Timestamp createDate) {
        this.num = num;
        this.title = title;
        this.post = post;
        this.hit = hit;
        this.imgurl = imgurl;
        this.createDate = createDate;        
    }

    public NoticeDto(Notice notice) {
        this.num = notice.getNum();
        this.title = notice.getTitle();
        this.post = notice.getPost();
        this.hit = notice.getHit();
        this.imgurl = notice.getImgurl();
        this.createDate = notice.getCreateDate();
        this.modifyDate = notice.getModifyDate();
    }
}
