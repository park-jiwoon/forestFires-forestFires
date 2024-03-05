package com.ZeroWaveV2.FireAlertV2.dto;

import com.ZeroWaveV2.FireAlertV2.constants.ErrorMessages;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
public class UpdateUserDto {
	@Size(min = 6, message = ErrorMessages.PASSWORD_MIN_LENGTH)
	private String currentPassword;
	@Size(min = 6, message = ErrorMessages.PASSWORD_MIN_LENGTH)
    private String newPassword;
}
