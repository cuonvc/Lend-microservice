package com.lend.mailservice.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class EmailTo {
    private String sendTo;
    private String subject;
    private String content;
}
