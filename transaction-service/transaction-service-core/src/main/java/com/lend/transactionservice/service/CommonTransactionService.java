package com.lend.transactionservice.service;

import com.lend.transactionservice.entity.Transaction;
import com.lend.transactionservice.enumerate.ClientRole;
import com.lend.transactionservice.response.TransactionResponseDetail;
import com.lend.transactionservice.response.TransactionResponseView;

public interface CommonTransactionService {

    Transaction authorizeOwner(String id, ClientRole clientRole);

    Transaction authorizeOwnerAndManager(String id, ClientRole clientRole);

    TransactionResponseView convertEntityToView(Transaction transaction);

    TransactionResponseDetail convertEntityToDetail(Transaction transaction);
}
