package com.lender.transactionservice.service.impl;

import com.lender.authserviceshare.payload.response.UserResponse;
import com.lender.baseservice.constant.enumerate.Status;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.ResponseFactory;
import com.lender.productserviceshare.payload.response.ProductResponse;
import com.lender.transactionservice.configuration.CustomUserDetail;
import com.lender.transactionservice.entity.Transaction;
import com.lender.transactionservice.enumerate.ClientRole;
import com.lender.transactionservice.enumerate.PaymentStatus;
import com.lender.transactionservice.enumerate.TransactionStatus;
import com.lender.transactionservice.exception.APIException;
import com.lender.transactionservice.exception.ResourceNotFoundException;
import com.lender.transactionservice.mapper.TransactionMapper;
import com.lender.transactionservice.payload.request.TransactionRequest;
import com.lender.transactionservice.repository.TransactionRepository;
import com.lender.transactionservice.response.TransactionResponseDetail;
import com.lender.transactionservice.response.TransactionResponseRaw;
import com.lender.transactionservice.response.TransactionResponseView;
import com.lender.transactionservice.service.BorrowerService;
import com.lender.transactionservice.service.CommonTransactionService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {

    private final TransactionRepository repository;
    private final RestTemplate restTemplate;
    private final TransactionMapper transactionMapper;
    private final ResponseFactory responseFactory;
    private final CommonTransactionService commonTransactionService;

    @Override
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> initTransaction(TransactionRequest request) {
        CustomUserDetail owner = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Transaction transaction = new Transaction();
        transaction.setBorrowerId(owner.getId());
        mapTransaction(transaction, request);

        return responseFactory.success("Success",
                transactionMapper.entityToRaw(repository.save(transaction)));
    }

    @Override
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> editTransaction(String id, TransactionRequest request) {
        CustomUserDetail owner = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Transaction transaction = repository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Giao dịch", "id", id));

        if (!owner.getId().equals(transaction.getBorrowerId())) {
            return responseFactory.fail(HttpStatus.UNAUTHORIZED, "Không được phép truy cập", null);
        } else if (transaction.getAcceptedDate() != null) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Bạn không thể chỉnh sửa khi người cho thuê đã tiếp nhận giao dịch", null);
        }

        transaction.setBorrowerId(owner.getId());
        mapTransaction(transaction, request);

        return responseFactory.success("Success",
                transactionMapper.entityToRaw(repository.save(transaction)));
    }

    @Override
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> cancelTransaction(String id) {
        Transaction transaction = Optional.ofNullable(commonTransactionService.authorizeOwnerAndManager(id, ClientRole.BORROWER))
                        .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Không được phép truy cập"));

        if (transaction.getAcceptedDate() != null) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Bạn không thể hủy khi người cho thuê đã tiếp nhận giao dịch", null);
        }

        transaction.setTransactionStatus(TransactionStatus.CANCELED);
        transaction = repository.save(transaction);
        return responseFactory.success("Success", transactionMapper.entityToRaw(transaction));
    }

    @Override
    public ResponseEntity<BaseResponse<String>> removeById(String id) {
        Transaction transaction = Optional.ofNullable(commonTransactionService.authorizeOwnerAndManager(id, ClientRole.BORROWER))
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Không được phép truy cập"));

        if (!transaction.getTransactionStatus().equals(TransactionStatus.PENDING)) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Bạn phải hủy giao dịch trước", null);
        }

        repository.delete(transaction);
        return responseFactory.success("Success", "Xóa thành công");
    }

    @Override
    public ResponseEntity<BaseResponse<String>> deleteMultiTransaction(String[] ids) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResponse<TransactionResponseDetail>> detailById(String id) {
        Transaction transaction = Optional.ofNullable(commonTransactionService.authorizeOwnerAndManager(id, ClientRole.BORROWER))
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Không được phép truy cập"));

        TransactionResponseDetail detail = commonTransactionService.convertEntityToDetail(transaction);
        return responseFactory.success("Success", detail);
    }

    @Override
    public ResponseEntity<BaseResponse<List<TransactionResponseView>>> getByBorrower(String status) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean statusMatch = Stream.of(TransactionStatus.values())
                .map(t -> t.name())
                .toList()
                .contains(status);
        if (!statusMatch) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Trạng thái giao dich không hợp lệ", new ArrayList<>());
        }

        List<TransactionResponseView> transactions = repository.findByBorrowerAndStatus(userDetail.getId(), TransactionStatus.valueOf(status))
                .stream().map(commonTransactionService::convertEntityToView)
                .toList();

        return responseFactory.success("Success", transactions);
    }

    private String getBillCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5).toUpperCase();
    }

    private void mapTransaction(Transaction transaction, TransactionRequest request) {

        ProductResponse product = Optional
                .ofNullable(restTemplate.exchange(
                        "http://PRODUCT-SERVICE/api/internal/product/" + request.getProductId(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<BaseResponse<ProductResponse>>() {}
                ).getBody().getData())
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm", "id", request.getProductId()));

        transaction.setLenderId(product.getUserId());
        transaction.setProductId(product.getId());
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setPaymentStatus(PaymentStatus.UNPAID);
        transaction.setQuantity(request.getQuantity());
        transaction.setAmount(product.getStandardPrice() * request.getQuantity());
        transaction.setBillCode(getBillCode());
        transaction.setBorrowerAddress(request.getBorrowerAddress());
    }
}
