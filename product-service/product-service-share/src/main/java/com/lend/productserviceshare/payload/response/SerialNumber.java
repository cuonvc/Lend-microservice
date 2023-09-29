package com.lend.productserviceshare.payload.response;

import com.lend.baseservice.constant.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SerialNumber {
    private String value;
    private Status status = Status.ACTIVE;
}
