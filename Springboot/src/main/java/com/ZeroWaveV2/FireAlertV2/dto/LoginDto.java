package com.ZeroWaveV2.FireAlertV2.dto;

import com.ZeroWaveV2.FireAlertV2.constants.ErrorMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginDto {

    @NotBlank(message = ErrorMessages.CONTACT_REQUIRED)
    private String hp;
    
    @Size(min = 6, message = ErrorMessages.PASSWORD_MIN_LENGTH)
    @NotBlank(message = ErrorMessages.PASSWORD_REQUIRED)
    private String password;

    public LoginDto(String hp, String password) {
        this.hp = hp;
        this.password = password;
    }
}

