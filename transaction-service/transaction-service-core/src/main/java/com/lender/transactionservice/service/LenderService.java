package com.lender.transactionservice.service;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.transactionservice.response.TransactionResponseRaw;
import com.lender.transactionservice.response.TransactionResponseView;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LenderService {

    ResponseEntity<BaseResponse<TransactionResponseRaw>> acceptTransaction(String id, String action);

    ResponseEntity<BaseResponse<List<TransactionResponseView>>> getByLender();

}
