package com.lender.transactionservice.service;

import com.lender.transactionservice.entity.Transaction;
import com.lender.transactionservice.enumerate.ClientRole;
import com.lender.transactionservice.response.TransactionResponseDetail;
import com.lender.transactionservice.response.TransactionResponseView;

public interface CommonTransactionService {

    Transaction authorizeOwner(String id, ClientRole clientRole);

    Transaction authorizeOwnerAndManager(String id, ClientRole clientRole);

    TransactionResponseView convertEntityToView(Transaction transaction);

    TransactionResponseDetail convertEntityToDetail(Transaction transaction);
}
