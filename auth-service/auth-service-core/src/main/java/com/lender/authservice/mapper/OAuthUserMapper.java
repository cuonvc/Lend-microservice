package com.lender.authservice.mapper;

import com.lender.authservice.payload.OAuthUserInfo;
import com.lender.authservice.payload.response.GithubResponseUser;
import com.lender.authservice.payload.response.GoogleResponseUser;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface OAuthUserMapper {

    OAuthUserInfo toUserInfo(GoogleResponseUser user);

    @Mapping(source = "fullName", target = "firstName", qualifiedByName = "getFirstNameGithub")
    @Mapping(source = "fullName", target = "lastName", qualifiedByName = "getLastNameGithub")
    OAuthUserInfo toUserInfo(GithubResponseUser user);

    @Named("getFirstNameGithub")
    default String getFirstName(String fullName) {
        return fullName.split(" ")[0];
    }

    @Named("getLastNameGithub")
    default String getLastName(String fullName) {
        return fullName.split(" ")[1];
    }
}
