package com.lender.transactionservice.mapper;

import com.lender.transactionservice.entity.Transaction;
import com.lender.transactionservice.repository.TransactionRepository;
import com.lender.transactionservice.response.TransactionResponseRaw;
import com.lender.transactionservice.response.TransactionResponseView;
import org.mapstruct.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface TransactionMapper {

    TransactionResponseRaw entityToRaw(Transaction transaction);

    TransactionResponseView entityToView(Transaction transaction);

}
