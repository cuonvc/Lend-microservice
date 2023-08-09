package com.lender.productservice.configuration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomUserDetail implements Serializable {

    private String email;
    @JsonProperty(value = "grantedAuthorities")
    private List<String> grantedAuthorities;

}
