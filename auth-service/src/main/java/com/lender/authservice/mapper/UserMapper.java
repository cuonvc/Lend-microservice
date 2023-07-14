package com.lender.authservice.mapper;

import com.lender.authservice.entity.User;
import com.lender.authservice.payload.request.ProfileRequest;
import com.lender.authservice.payload.request.RegRequest;
import com.lender.authservice.payload.response.UserResponse;
import com.lender.resourceservice.constants.SharingConstants;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User requestToEntity(RegRequest userRequest);

    @Mapping(source = "avatarUrl", target = "avatarUrl", qualifiedByName = "pathToUrl")
    UserResponse entityToResponse(User user);

    User profileToEntity(ProfileRequest profileRequest, @MappingTarget User user);

    @Named("pathToUrl")
    static String pathToUrl(String path) {
        return SharingConstants.RESOURCE_URL + path;
    }
}
