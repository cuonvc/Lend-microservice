package com.lender.transactionservice.service.impl;

import com.lender.authserviceshare.payload.response.UserResponse;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.productserviceshare.payload.response.ProductResponse;
import com.lender.transactionservice.entity.Transaction;
import com.lender.transactionservice.exception.ResourceNotFoundException;
import com.lender.transactionservice.mapper.TransactionMapper;
import com.lender.transactionservice.response.TransactionResponseView;
import com.lender.transactionservice.service.CommonTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonTransactionServiceImpl implements CommonTransactionService {

    private final RestTemplate restTemplate;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionResponseView convertEntityToView(Transaction transaction) {
        TransactionResponseView view = transactionMapper.entityToView(transaction);
        view.setBorrowerName(getUserName(transaction.getBorrowerId()));
        view.setLenderName(getUserName(transaction.getLenderId()));
        view.setProductName(getProductName(transaction.getProductId()));
        return view;
    }

    private String getUserName(String id) {
        UserResponse user = Optional
                .ofNullable(restTemplate.exchange(
                        "http://AUTH-SERVICE/api/auth/account/" + id,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<BaseResponse<UserResponse>>() {}
                ).getBody().getData())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return user.getFirstName() + " " + user.getLastName();
    }

    private String getProductName(String id) {
        return Optional
                .ofNullable(restTemplate.exchange(
                        "http://PRODUCT-SERVICE/api/internal/product/" + id,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<BaseResponse<ProductResponse>>() {}
                ).getBody().getData().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }
}
