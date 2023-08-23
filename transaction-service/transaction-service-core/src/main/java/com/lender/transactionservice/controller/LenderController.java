package com.lender.transactionservice.controller;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.transactionservice.response.TransactionResponseDetail;
import com.lender.transactionservice.response.TransactionResponseRaw;
import com.lender.transactionservice.response.TransactionResponseView;
import com.lender.transactionservice.service.LenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class LenderController {

    private final LenderService lenderService;

    //only owner
    @PostMapping("/{id}")
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> action(@PathVariable("id") String id,
                                                                       @RequestParam("action") String action) {
        return lenderService.acceptTransaction(id, action.toUpperCase());
    }

    @GetMapping("/detail/{id}/lender")
    public ResponseEntity<BaseResponse<TransactionResponseDetail>> detail(@PathVariable("id") String id) {
        return lenderService.detailById(id);
    }

    @GetMapping("/all/lender")
    public ResponseEntity<BaseResponse<List<TransactionResponseView>>> viewAllByLender() {
        return lenderService.getByLender();
    }

}
