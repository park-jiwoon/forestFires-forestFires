package com.ZeroWaveV2.FireAlertV2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import com.ZeroWaveV2.FireAlertV2.constants.ErrorMessages;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FireStationDto {
	@NotBlank(message = ErrorMessages.FS_REQUIRED)
    private String fs;

    @NotBlank(message = ErrorMessages.PASSWORD_REQUIRED)
    private String password;
    
    private String fsName;
    private String fsAdd;
    private String fph;

    public FireStationDto(String fs, String password) {
        this.fs = fs;
        this.password = password;
    }
    
    public FireStationDto(String fsName, String fsAdd, String fph) {
    	this.fsName = fsName;
    	this.fsAdd = fsAdd;
    	this.fph = fph;
    }
}



