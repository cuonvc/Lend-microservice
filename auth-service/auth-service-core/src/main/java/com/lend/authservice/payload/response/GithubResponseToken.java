package com.lend.authservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubResponseToken {
    private String access_token;
    private String scope;
    private String token_type;
}
