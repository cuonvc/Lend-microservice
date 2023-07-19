package com.lender.authserviceshare.payload.response;

import com.lender.baseservice.payload.response.PageResponse;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class PageResponseUsers extends PageResponse<UserResponse> {

}
