package com.lend.transactionservice.controller;

import com.lend.transactionservice.payload.request.TransactionRequest;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.transactionservice.response.TransactionResponseDetail;
import com.lend.transactionservice.response.TransactionResponseRaw;
import com.lend.transactionservice.response.TransactionResponseView;
import com.lend.transactionservice.service.BorrowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowService;

    //only owner
    @PostMapping("/init")
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> init(@Valid @RequestBody TransactionRequest request) {
        return borrowService.initTransaction(request);
    }

    //only owner
    @PutMapping("/edit/{id}")
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> edit(@PathVariable("id") String id,
                                                                                @Valid @RequestBody TransactionRequest request) {
        return borrowService.editTransaction(id, request);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> cancel(@PathVariable("id") String id) {
        return borrowService.cancelTransaction(id);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<BaseResponse<String>> remove(@PathVariable("id") String id) {
        return borrowService.removeById(id);
    }

    @DeleteMapping("/delete/{ids}")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable("ids") String[] ids) {
        return null;
    }

    @GetMapping("/detail/{id}/borrower")
    public ResponseEntity<BaseResponse<TransactionResponseDetail>> detail(@PathVariable("id") String id) {
        return borrowService.detailById(id);
    }

    @GetMapping("/all/borrower")
    public ResponseEntity<BaseResponse<List<TransactionResponseView>>> viewAllByBorrower(@RequestParam(value = "status") String status) {
        return borrowService.getByBorrower(status.toUpperCase());
    }

}
