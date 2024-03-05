package com.ZeroWaveV2.FireAlertV2.dto;

import java.sql.Timestamp;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberDto {
	private String hp;
	private String userName;
	private Timestamp CreateDate;
}
