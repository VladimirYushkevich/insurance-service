package com.company.insurance.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Custom page class.
 */

@Builder
@Getter
public class PageDTO<T> {
    private Integer totalPages;
    private Long totalEntries;
    private List<T> entries;
}