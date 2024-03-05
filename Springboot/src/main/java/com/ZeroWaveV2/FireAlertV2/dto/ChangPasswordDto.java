package com.ZeroWaveV2.FireAlertV2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ChangPasswordDto {
	private String hp;
	private String username;
	private String password;
}
