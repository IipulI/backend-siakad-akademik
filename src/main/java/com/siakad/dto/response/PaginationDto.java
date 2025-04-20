package com.siakad.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PaginationDto {
    private int currentPage;
    private int perPage;
    private int totalPages;
    private int totalItems;

    public static PaginationDto fromPage(Page<?> page) {
        PaginationDto pagination = new PaginationDto();
        pagination.setCurrentPage(page.getNumber() + 1);
        pagination.setPerPage(page.getSize());
        pagination.setTotalPages(page.getTotalPages());
        pagination.setTotalItems((int) page.getTotalElements());
        return pagination;
    }
}
