package com.lender.authservice.mapper;

import com.lender.authservice.entity.User;
import com.lender.authservice.payload.request.RegRequest;
import com.lender.authservice.payload.response.UserResponse;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User requestToEntity(RegRequest userRequest);
    UserResponse entityToResponse(User user);
}
