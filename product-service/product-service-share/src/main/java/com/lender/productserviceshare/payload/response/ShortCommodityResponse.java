package com.lender.productserviceshare.payload.response;

import com.lender.productserviceshare.enumerate.LendType;
import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.enumerate.TimeFrame;
import com.lender.productserviceshare.enumerate.TransactionMethod;

import java.time.LocalDateTime;
import java.util.List;

public class ShortCommodityResponse {
    
    private String id;

    private Double standardPrice;

    private ProductResponse productResponse;
    
}
