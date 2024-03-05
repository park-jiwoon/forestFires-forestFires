package com.ZeroWaveV2.FireAlertV2.dto;

import com.ZeroWaveV2.FireAlertV2.constants.ErrorMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AdminDto {
	@NotBlank(message = ErrorMessages.CONTACT_REQUIRED)
    private String id;
    
    @Size(min = 6, message = ErrorMessages.PASSWORD_MIN_LENGTH)
    @NotBlank(message = ErrorMessages.PASSWORD_REQUIRED)
    private String password;

    public AdminDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
