package com.luv2code.api.security.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
