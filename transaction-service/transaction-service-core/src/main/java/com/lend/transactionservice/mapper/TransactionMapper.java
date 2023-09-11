package com.lend.transactionservice.mapper;

import com.lend.transactionservice.entity.Transaction;
import com.lend.transactionservice.response.TransactionResponseDetail;
import com.lend.transactionservice.response.TransactionResponseRaw;
import com.lend.transactionservice.response.TransactionResponseView;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface TransactionMapper {

    TransactionResponseRaw entityToRaw(Transaction transaction);

    TransactionResponseView entityToView(Transaction transaction);

    TransactionResponseDetail entityToDetail(Transaction transaction);

}
