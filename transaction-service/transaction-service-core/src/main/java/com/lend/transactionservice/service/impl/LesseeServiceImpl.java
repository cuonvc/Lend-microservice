package com.lend.transactionservice.service.impl;

import com.lend.productserviceshare.payload.request.CommodityRequest;
import com.lend.productserviceshare.payload.response.CommodityResponse;
import com.lend.productserviceshare.payload.response.SerialListValue;
import com.lend.productserviceshare.payload.response.SerialNumber;
import com.lend.transactionservice.entity.Transaction;
import com.lend.transactionservice.exception.APIException;
import com.lend.transactionservice.mapper.TransactionMapper;
import com.lend.transactionservice.payload.request.TransactionRequest;
import com.lend.transactionservice.payload.request.TransactionUpdateRequest;
import com.lend.transactionservice.service.CommonTransactionService;
import com.lend.authserviceshare.payload.response.UserResponse;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.baseservice.payload.response.ResponseFactory;
import com.lend.productserviceshare.payload.response.ProductResponse;
import com.lend.transactionservice.configuration.CustomUserDetail;
import com.lend.transactionservice.enumerate.ClientRole;
import com.lend.transactionservice.enumerate.PaymentStatus;
import com.lend.transactionservice.enumerate.TransactionStatus;
import com.lend.transactionservice.exception.ResourceNotFoundException;
import com.lend.transactionservice.repository.TransactionRepository;
import com.lend.transactionservice.response.TransactionResponseDetail;
import com.lend.transactionservice.response.TransactionResponseRaw;
import com.lend.transactionservice.response.TransactionResponseView;
import com.lend.transactionservice.service.LesseeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class LesseeServiceImpl implements LesseeService {

    private final TransactionRepository repository;
    private final RestTemplate restTemplate;
    private final TransactionMapper transactionMapper;
    private final ResponseFactory responseFactory;
    private final CommonTransactionService commonTransactionService;
    private final StreamBridge streamBridge;

    @Override
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> initTransaction(TransactionRequest request) {
        CustomUserDetail owner = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Transaction transaction = new Transaction();
        transaction.setLesseeId(owner.getId());
        mapTransaction(transaction, request);

        return responseFactory.success("Success",
                transactionMapper.entityToRaw(repository.save(transaction)));
    }

    @Override
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> editTransaction(String id, TransactionUpdateRequest request) {
        CustomUserDetail owner = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Transaction transaction = repository.findByIdAndIsActive(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Giao dịch", "id", id));

        if (!owner.getId().equals(transaction.getLesseeId())) {
            return responseFactory.fail(HttpStatus.UNAUTHORIZED, "Không được phép truy cập", null);
        } else if (transaction.getAcceptedDate() != null) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Bạn không thể chỉnh sửa khi giao dịch đã được tiếp nhận", null);
        }

        transaction.setLesseeAddress(request.getLesseeAddress());
        return responseFactory.success("Success",
                transactionMapper.entityToRaw(repository.save(transaction)));
    }

    @Override
    public ResponseEntity<BaseResponse<TransactionResponseRaw>> cancelTransaction(String id) {
        Transaction transaction = Optional.ofNullable(commonTransactionService.authorizeOwnerAndManager(id, ClientRole.LESSEE))
                        .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Không được phép truy cập"));

        if (transaction.getAcceptedDate() != null) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Bạn không thể hủy khi giao dịch đã được tiếp nhận", null);
        }

        transaction.setTransactionStatus(TransactionStatus.CANCELED);
        transaction = repository.save(transaction);
        return responseFactory.success("Success", transactionMapper.entityToRaw(transaction));
    }

    @Override
    public ResponseEntity<BaseResponse<String>> removeById(String id) {
        Transaction transaction = Optional.ofNullable(commonTransactionService.authorizeOwnerAndManager(id, ClientRole.LESSEE))
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Không được phép truy cập"));

        if (!transaction.getTransactionStatus().equals(TransactionStatus.PENDING)) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Bạn phải hủy giao dịch trước", null);
        }

        repository.delete(transaction);
        return responseFactory.success("Success", "Xóa thành công");
    }

    @Override
    public ResponseEntity<BaseResponse<String>> deleteMultiTransaction(String[] ids) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResponse<TransactionResponseDetail>> detailById(String id) {
        Transaction transaction = Optional.ofNullable(commonTransactionService.authorizeOwnerAndManager(id, ClientRole.LESSEE))
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Không được phép truy cập"));

        TransactionResponseDetail detail = commonTransactionService.convertEntityToDetail(transaction);
        return responseFactory.success("Success", detail);
    }

    @Override
    public ResponseEntity<BaseResponse<List<TransactionResponseView>>> getByBorrower(String status) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean statusMatch = Stream.of(TransactionStatus.values())
                .map(t -> t.name())
                .toList()
                .contains(status);
        if (!statusMatch) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Trạng thái giao dich không hợp lệ", new ArrayList<>());
        }

        List<TransactionResponseView> transactions = repository
                .findAllByLesseeIdAndTransactionStatus(userDetail.getId(), TransactionStatus.valueOf(status))
                .stream().map(commonTransactionService::convertEntityToView)
                .toList();

        return responseFactory.success("Success", transactions);
    }

    private String getBillCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5).toUpperCase();
    }

    private void mapTransaction(Transaction transaction, TransactionRequest request) {
        Set<String> serialNumbers = new HashSet<>(request.getSerialNumbers());

        CommodityResponse commodity = Optional
                .ofNullable(restTemplate.exchange(
                        "http://PRODUCT-SERVICE/api/internal/commodity/" + request.getCommodityId(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<BaseResponse<CommodityResponse>>() {}
                ).getBody().getData())
                .orElseThrow(() -> new ResourceNotFoundException("Mặt hàng", "id", request.getCommodityId()));

        if (commodity.getUserId().equals(transaction.getLesseeId())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Bạn không thể order mặt hàng của mình");
        }

        Set<String> validNumbers = commodity.getSerialNumbers().stream()
                .filter(number -> number.getStatus().equals(Status.ACTIVE))
                .map(SerialNumber::getValue)
                .collect(Collectors.toSet());

        Set<String> acceptedNumbers = serialNumbers.stream().map(number -> {
            if (!validNumbers.contains(number)) {
                throw new APIException(HttpStatus.BAD_REQUEST, "Mã Sản phẩm " + number + " không tồn tại");
            }
            return number;
        }).collect(Collectors.toSet());
        log.info("Trigger - {}", acceptedNumbers);

        transaction.setLessorId(commodity.getUserId());
        transaction.setCommodityId(commodity.getId());
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setPaymentStatus(PaymentStatus.UNPAID);
        transaction.setSerialNumbers(acceptedNumbers);
        transaction.setAmount(commodity.getStandardPrice() * acceptedNumbers.size());
        transaction.setBillCode(getBillCode());
        transaction.setLesseeAddress(request.getLesseeAddress());

        Message<SerialListValue> message = MessageBuilder.withPayload(SerialListValue.builder()
                        .list(acceptedNumbers)
                        .status(Status.INACTIVE)
                        .build())
                .setHeader(KafkaHeaders.KEY, commodity.getId().getBytes())
                .build();

        streamBridge.send("serials-action-request", message);
    }
}
