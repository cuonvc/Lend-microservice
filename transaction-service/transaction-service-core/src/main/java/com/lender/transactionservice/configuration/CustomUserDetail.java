package com.lender.transactionservice.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomUserDetail implements Serializable {

    private String id;

    private String email;

    @JsonProperty(value = "grantedAuthorities")
    private List<String> grantedAuthorities;

}
