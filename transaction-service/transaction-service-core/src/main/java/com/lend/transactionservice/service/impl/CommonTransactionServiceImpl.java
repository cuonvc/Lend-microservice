package com.lend.transactionservice.service.impl;

import com.lend.productserviceshare.payload.response.CommodityResponse;
import com.lend.transactionservice.entity.Transaction;
import com.lend.transactionservice.exception.APIException;
import com.lend.transactionservice.service.CommonTransactionService;
import com.lend.authserviceshare.payload.response.UserResponse;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.productserviceshare.payload.response.ProductResponse;
import com.lend.transactionservice.configuration.CustomUserDetail;
import com.lend.transactionservice.enumerate.ClientRole;
import com.lend.transactionservice.exception.ResourceNotFoundException;
import com.lend.transactionservice.mapper.TransactionMapper;
import com.lend.transactionservice.repository.TransactionRepository;
import com.lend.transactionservice.response.TransactionResponseDetail;
import com.lend.transactionservice.response.TransactionResponseView;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonTransactionServiceImpl implements CommonTransactionService {

    private final RestTemplate restTemplate;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository repository;

    @Override
    public Transaction authorizeOwner(String id, ClientRole clientRole) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Transaction transaction = repository.findByIdAndIsActive(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Giao dịch", "id", id));

        if (!getClientRequestId(clientRole, transaction).equals(userDetail.getId())) {
            return null;
        }
        return transaction;
    }

    @Override
    public Transaction authorizeOwnerAndManager(String id, ClientRole clientRole) {
        Transaction transaction = repository.findByIdAndIsActive(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Giao dịch", "id", id));

        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetail.getGrantedAuthorities().get(0).equals("USER")
                && !userDetail.getId().equals(getClientRequestId(clientRole, transaction))) {
            return null;
        }

        return transaction;
    }

    private String getClientRequestId(ClientRole clientRole, Transaction transaction) {
        if (clientRole.equals(ClientRole.LESSEE)) {
            return transaction.getLesseeId();
        } else if (clientRole.equals(ClientRole.LESSOR)) {
            return transaction.getLessorId();
        } else {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Không biết tại sao lỗi, vui lòng liên hệ Admin :D");
        }
    }

    @Override
    public TransactionResponseView convertEntityToView(Transaction transaction) {
        TransactionResponseView view = transactionMapper.entityToView(transaction);
        view.setLesseeName(getUserName(transaction.getLesseeId()));
        view.setLessorName(getUserName(transaction.getLessorId()));
        view.setProductName(getProductInfo(transaction.getCommodityId()).getProduct().getName());
        return view;
    }

    @Override
    public TransactionResponseDetail convertEntityToDetail(Transaction transaction) {
        TransactionResponseDetail detail = transactionMapper.entityToDetail(transaction);
        detail.setLessee(getUserInfo(transaction.getLesseeId()));
        detail.setLessor(getUserInfo(transaction.getLessorId()));
        detail.setProduct(getProductInfo(transaction.getCommodityId()).getProduct());
        return detail;
    }

    private String getUserName(String id) {
        UserResponse userResponse = getUserInfo(id);
        return userResponse.getFirstName() + " " + userResponse.getLastName();
    }

    private UserResponse getUserInfo(String id) {
        return Optional
                .ofNullable(restTemplate.exchange(
                        "http://AUTH-SERVICE/api/auth/account/" + id,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<BaseResponse<UserResponse>>() {}
                ).getBody().getData())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    private CommodityResponse getProductInfo(String id) {
        return Optional
                .ofNullable(restTemplate.exchange(
                        "http://PRODUCT-SERVICE/api/internal/commodity/" + id,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<BaseResponse<CommodityResponse>>() {}
                ).getBody().getData())
                .orElseThrow(() -> new ResourceNotFoundException("Mặt hàng", "id", id));
    }
}
