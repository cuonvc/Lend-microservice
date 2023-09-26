package com.lend.productservice.mapper;

import com.lend.productservice.entity.Commodity;
import com.lend.productserviceshare.enumerate.ProductState;
import com.lend.productserviceshare.enumerate.TimeFrame;
import com.lend.productserviceshare.enumerate.TransactionMethod;
import com.lend.productserviceshare.payload.request.CommodityRequest;
import com.lend.productserviceshare.payload.response.CommodityResponse;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
@Component
public interface CommodityMapper {

    @Mapping(source = "timeFrames", target = "timeFrames", qualifiedByName = "timeFramestoString")
    @Mapping(source = "transactionMethods", target = "transactionMethods", qualifiedByName = "transactionMethodsToString")
    @Mapping(source = "state", target = "code", qualifiedByName = "generateCode")
    @Mapping(source = "serialNumbers", target = "serialNumbers", ignore = true)
    @Mapping(source = "serialNumbers", target = "remaining", qualifiedByName = "getRemaining")
    Commodity requestToEntity(CommodityRequest commodityRequest);

    @Mapping(source = "timeFrames", target = "timeFrames", qualifiedByName = "timeFramestoString")
    @Mapping(source = "transactionMethods", target = "transactionMethods", qualifiedByName = "transactionMethodsToString")
    @Mapping(source = "serialNumbers", target = "serialNumbers", ignore = true)
    void requestToEntity(CommodityRequest commodityRequest, @MappingTarget Commodity commodity);

    @Mapping(source = "timeFrames", target = "timeFrames", qualifiedByName = "timeFramesToList")
    @Mapping(source = "transactionMethods", target = "transactionMethods", qualifiedByName = "transactionMethodsToList")
    CommodityResponse entityToResponse(Commodity commodity);

    @Named("getRemaining")
    static Integer getRemaining(Set<String> serialNumbers) {
        return serialNumbers.size();
    }

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
