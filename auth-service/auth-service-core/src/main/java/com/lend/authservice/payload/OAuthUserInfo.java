package com.lend.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
}
