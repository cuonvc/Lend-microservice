package com.lender.productservice.mapper;

import com.lender.productservice.entity.Commodity;
import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.enumerate.TimeFrame;
import com.lender.productserviceshare.enumerate.TransactionMethod;
import com.lender.productserviceshare.payload.request.CommodityRequest;
import com.lender.productserviceshare.payload.response.CommodityResponse;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
@Component
public interface CommodityMapper {

    @Mapping(source = "timeFrames", target = "timeFrames", qualifiedByName = "timeFramestoString")
    @Mapping(source = "transactionMethods", target = "transactionMethods", qualifiedByName = "transactionMethodsToString")
    @Mapping(source = "state", target = "code", qualifiedByName = "generateCode")
    Commodity requestToEntity(CommodityRequest commodityRequest);

    @Mapping(source = "timeFrames", target = "timeFrames", qualifiedByName = "timeFramesToList")
    @Mapping(source = "transactionMethods", target = "transactionMethods", qualifiedByName = "transactionMethodsToList")
    CommodityResponse entityToResponse(Commodity commodity);

    @Named("generateCode")
    static String generateProductCode(ProductState state) {
        Random random = new Random();
        return state.name().charAt(0) + String.valueOf(random.nextInt(10000));
    }

    @Named("timeFramestoString")
    static String timeFramesToString(List<TimeFrame> list) {
        return list.toString();
    }

    @Named("transactionMethodsToString")
    static String transactionMethodsToString(List<TransactionMethod> list) {
        return list.toString();
    }

    @Named("timeFramesToList")
    static List<TimeFrame> timeFramesToList(String timeFrames) {
        return Arrays.stream(timeFrames
                        .substring(1, timeFrames.length() - 1)
                        .split(", ")).toList()
                .stream().map(TimeFrame::valueOf).toList();
    }

    @Named("transactionMethodsToList")
    static List<TransactionMethod> transactionMethodsToList(String methods) {
        return Arrays.stream(methods
                        .substring(1, methods.length() - 1)
                        .split(", ")).toList()
                .stream().map(TransactionMethod::valueOf).toList();
    }
}
