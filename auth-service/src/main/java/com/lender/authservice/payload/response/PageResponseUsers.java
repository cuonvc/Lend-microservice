package com.lender.authservice.payload.response;

import com.lender.baseservice.payload.response.PageResponse;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class PageResponseUsers extends PageResponse<UserResponse> {

}
