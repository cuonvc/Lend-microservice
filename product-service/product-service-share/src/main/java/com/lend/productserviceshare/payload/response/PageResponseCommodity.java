package com.lend.productserviceshare.payload.response;

import com.lend.baseservice.payload.response.PageResponse;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class PageResponseCommodity extends PageResponse<CommodityResponse> {

}
