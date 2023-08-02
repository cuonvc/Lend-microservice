package com.lender.authservice.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GithubResponseUser {

    @JsonProperty("login")
    private String username;

    private String id;

    @JsonProperty("node_id")
    private String nodeId;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("url")
    private String url;

    @JsonProperty("repos_url")
    private String reposUrl;

    private String type;

    @JsonProperty("site_admin")
    private Boolean siteAdmin;

    @JsonProperty("name")
    private String fullName;

    private String company;

    private String blog;

    private String location;

    private String email;

    private String bio;

    @JsonProperty("public_repos")
    private String publicRepos;

    private Integer followers;

    private Integer following;

}
