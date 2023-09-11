package com.lend.transactionservice.service.impl;

import com.lend.authserviceshare.payload.response.UserResponse;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.baseservice.payload.response.ResponseFactory;
import com.lend.productserviceshare.payload.response.ProductResponse;
import com.lend.transactionservice.configuration.CustomUserDetail;
import com.lend.transactionservice.entity.Transaction;
import com.lend.transactionservice.enumerate.ClientRole;
import com.lend.transactionservice.enumerate.TransactionStatus;
import com.lend.transactionservice.exception.APIException;
import com.lend.transactionservice.mapper.TransactionMapper;
import com.lend.transactionservice.repository.TransactionRepository;
import com.lend.transactionservice.response.TransactionResponseDetail;
import com.lend.transactionservice.response.TransactionResponseRaw;
import com.lend.transactionservice.response.TransactionResponseView;
import com.lend.transactionservice.service.CommonTransactionService;
import com.lend.transactionservice.service.LenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LenderServiceImpl implements LenderService {

    private final TransactionRepository repository;
    private final ResponseFactory responseFactory;
    private final TransactionMapper transactionMapper;
    private final CommonTransactionService commonTransactionService;

    @Override
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> acceptTransaction(String id, String action) {
        Transaction transaction = Optional.ofNullable(commonTransactionService.authorizeOwner(id, ClientRole.LENDER))
                        .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Không được phép truy cập"));
        switch (action) {
            case "ACCEPT" -> transaction.setAcceptedDate(LocalDateTime.now());
            case "REJECT" -> transaction.setTransactionStatus(TransactionStatus.REJECTED);
            default -> responseFactory.fail(HttpStatus.BAD_REQUEST, "Hành động không phù hợp", null);
        }

        return responseFactory.success("Success", transactionMapper.entityToRaw(repository.save(transaction)));
    }

    @Override
    public ResponseEntity<BaseResponse<TransactionResponseDetail>> detailById(String id) {
        Transaction transaction = Optional.ofNullable(commonTransactionService.authorizeOwnerAndManager(id, ClientRole.LENDER))
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Không được phép truy cập"));

        TransactionResponseDetail detail = commonTransactionService.convertEntityToDetail(transaction);
        return responseFactory.success("Success", detail);
    }

    @Override
    public ResponseEntity<BaseResponse<List<TransactionResponseView>>> getByLender() {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TransactionResponseView> transactions = repository.findByLender(userDetail.getId())
                .stream().map(commonTransactionService::convertEntityToView)
                .toList();

        return responseFactory.success("Success", transactions);
    }

}
