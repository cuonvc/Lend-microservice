package com.lend.transactionservice.controller;

import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.transactionservice.response.TransactionResponseDetail;
import com.lend.transactionservice.response.TransactionResponseRaw;
import com.lend.transactionservice.response.TransactionResponseView;
import com.lend.transactionservice.service.LessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class LessorController {

    private final LessorService lessorService;

    //only owner
    @PostMapping("/{id}")
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> action(@PathVariable("id") String id,
                                                                       @RequestParam("action") String action) {
        return lessorService.acceptTransaction(id, action.toUpperCase());
    }

    @GetMapping("/detail/{id}/lender")
    public ResponseEntity<BaseResponse<TransactionResponseDetail>> detail(@PathVariable("id") String id) {
        return lessorService.detailById(id);
    }

    @GetMapping("/all/lender")
    public ResponseEntity<BaseResponse<List<TransactionResponseView>>> viewAllByLender() {
        return lessorService.getByLender();
    }

}
