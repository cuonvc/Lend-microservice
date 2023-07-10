package com.lender.baseservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder(toBuilder = true)
@SuperBuilder(toBuilder = true)
public class PageResponse<T> {
    private List<T> content = new ArrayList<>();
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalItems;
    private Integer totalPages;
    private Boolean last;
}
