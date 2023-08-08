package com.lender.productserviceshare.payload.response;

import com.lender.baseservice.payload.response.PageResponse;
import com.lender.productserviceshare.payload.CategoryDto;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class PageResponseCategory extends PageResponse<CategoryDto> {

}
