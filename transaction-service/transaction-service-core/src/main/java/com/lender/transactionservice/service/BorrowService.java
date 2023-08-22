package com.lender.transactionservice.service;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.transactionservice.enumerate.TransactionStatus;
import com.lender.transactionservice.payload.request.TransactionRequest;
import com.lender.transactionservice.response.TransactionResponseRaw;
import com.lender.transactionservice.response.TransactionResponseView;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BorrowService {

    ResponseEntity<BaseResponse<TransactionResponseRaw>> initTransaction(TransactionRequest request);

    ResponseEntity<BaseResponse<TransactionResponseRaw>> editTransaction(String id, TransactionRequest request);

    ResponseEntity<BaseResponse<TransactionResponseRaw>> cancelTransaction(String id);

    ResponseEntity<BaseResponse<String>> removeById(String id);

    ResponseEntity<BaseResponse<String>> deleteMultiTransaction(String[] ids);

    ResponseEntity<BaseResponse<List<TransactionResponseView>>> getByBorrower(String status);
}
