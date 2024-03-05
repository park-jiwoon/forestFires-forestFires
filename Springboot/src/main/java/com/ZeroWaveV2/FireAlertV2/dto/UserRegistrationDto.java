package com.ZeroWaveV2.FireAlertV2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import com.ZeroWaveV2.FireAlertV2.constants.ErrorMessages;
@Data

public class UserRegistrationDto {

	@NotBlank(message = ErrorMessages.CONTACT_REQUIRED)
    private String hp;

    @Size(min = 6, message = ErrorMessages.PASSWORD_MIN_LENGTH)
    private String password;

    @NotBlank(message = ErrorMessages.USER_NAME_REQUIRED)
    private String userName;
      

    public UserRegistrationDto(String hp, String password, String userName) {
    	this.hp = hp;
        this.password = password;
        this.userName = userName;
    }    
}
