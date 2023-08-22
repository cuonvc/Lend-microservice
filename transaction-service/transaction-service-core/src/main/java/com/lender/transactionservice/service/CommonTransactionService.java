package com.lender.transactionservice.service;

import com.lender.transactionservice.entity.Transaction;
import com.lender.transactionservice.response.TransactionResponseView;

public interface CommonTransactionService {

    TransactionResponseView convertEntityToView(Transaction transaction);
}
