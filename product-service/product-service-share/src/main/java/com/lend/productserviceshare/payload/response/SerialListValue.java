package com.lend.productserviceshare.payload.response;

import com.lend.baseservice.constant.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SerialListValue {
    private Set<String> list;
    private Status status;
}
