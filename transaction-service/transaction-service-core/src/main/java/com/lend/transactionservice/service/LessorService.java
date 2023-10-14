package com.lend.transactionservice.service;

import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.transactionservice.response.TransactionResponseDetail;
import com.lend.transactionservice.response.TransactionResponseRaw;
import com.lend.transactionservice.response.TransactionResponseView;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LessorService {

    ResponseEntity<BaseResponse<TransactionResponseRaw>> acceptTransaction(String id, String action);

    ResponseEntity<BaseResponse<TransactionResponseDetail>> detailById(String id);

    ResponseEntity<BaseResponse<List<TransactionResponseView>>> getByLessor();

}
