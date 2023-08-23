package com.lender.transactionservice.service.impl;

import com.lender.authserviceshare.payload.response.UserResponse;
import com.lender.baseservice.constant.enumerate.Status;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.ResponseFactory;
import com.lender.productserviceshare.payload.response.ProductResponse;
import com.lender.transactionservice.configuration.CustomUserDetail;
import com.lender.transactionservice.entity.Transaction;
import com.lender.transactionservice.enumerate.ClientRole;
import com.lender.transactionservice.enumerate.TransactionStatus;
import com.lender.transactionservice.exception.APIException;
import com.lender.transactionservice.exception.ResourceNotFoundException;
import com.lender.transactionservice.mapper.TransactionMapper;
import com.lender.transactionservice.repository.TransactionRepository;
import com.lender.transactionservice.response.TransactionResponseDetail;
import com.lender.transactionservice.response.TransactionResponseRaw;
import com.lender.transactionservice.response.TransactionResponseView;
import com.lender.transactionservice.service.CommonTransactionService;
import com.lender.transactionservice.service.LenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
