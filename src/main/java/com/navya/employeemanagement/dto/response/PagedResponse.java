package com.navya.employeemanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PagedResponse<T> {

    @Schema(description = "List of data items for the current page")
    private final List<T> data;

    @Schema(description = "Current page number (zero-based index)", example = "0")
    private final int page;
    @Schema(description = "Number of items per page", example = "10")
    private final int size;
    @Schema(description = "Total number of elements", example = "100")
    private final long totalElements;
    @Schema(description = "Total number of pages", example = "10")
    private final int totalPages;
    @Schema(description = "Indicates if this is the first page", example = "true")
    private final boolean first;
    @Schema(description = "Indicates if this is the last page", example = "false")
    private final boolean last;

    public PagedResponse(List<T> data, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {

        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    public List<T> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }
}