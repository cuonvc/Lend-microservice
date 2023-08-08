package com.lender.productserviceshare.payload.response;

import com.lender.baseservice.payload.response.PageResponse;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class PageResponseProduct extends PageResponse<ProductResponse> {

}
