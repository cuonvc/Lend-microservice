package com.lend.authservice.mapper;

import com.lend.authservice.entity.User;
import com.lend.authserviceshare.payload.request.ProfileRequest;
import com.lend.authserviceshare.payload.request.RegRequest;
import com.lend.authserviceshare.payload.response.UserResponse;
//import com.lender.resourceservice.constants.SharingConstants;
import com.lend.baseservice.constant.ConstantVariable;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

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
        if (path != null && path.contains("http")) {
            return path;
        }
        return ConstantVariable.BASE_RESOURCE_DOMAIN + path;
    }
}
