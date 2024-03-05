package com.ZeroWaveV2.FireAlertV2.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor  // Lombok을 사용하여 기본 생성자 자동 생성
@AllArgsConstructor // Lombok을 사용하여 모든 필드를 포함한 생성자 자동 생성
public class GuestDto {
    private String hp; // 사용자 전화번호
}
