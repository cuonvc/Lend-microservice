package com.lend.transactionservice.service;

import com.lend.transactionservice.payload.request.TransactionRequest;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.transactionservice.response.TransactionResponseDetail;
import com.lend.transactionservice.response.TransactionResponseRaw;
import com.lend.transactionservice.response.TransactionResponseView;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LesseeService {

    ResponseEntity<BaseResponse<TransactionResponseRaw>> initTransaction(TransactionRequest request);

    ResponseEntity<BaseResponse<TransactionResponseRaw>> editTransaction(String id, TransactionRequest request);

    ResponseEntity<BaseResponse<TransactionResponseRaw>> cancelTransaction(String id);

    ResponseEntity<BaseResponse<String>> removeById(String id);

    ResponseEntity<BaseResponse<String>> deleteMultiTransaction(String[] ids);

    ResponseEntity<BaseResponse<TransactionResponseDetail>> detailById(String id);

    ResponseEntity<BaseResponse<List<TransactionResponseView>>> getByBorrower(String status);
}
