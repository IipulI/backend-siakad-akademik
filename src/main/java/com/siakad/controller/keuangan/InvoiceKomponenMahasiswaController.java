package com.siakad.controller.keuangan;

import com.siakad.dto.request.InvoiceKomponenMahasiswaReqDto;
import com.siakad.dto.request.InvoiceKomponenReqDto;
import com.siakad.dto.request.InvoiceMahasiswaReqDto;
import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.InvoiceKomponenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Invoice Komponen Mahasiswa")
@RestController
@RequestMapping("/keuangan/invoice-komponen-mahasiswa")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('KEUANGAN_UNIV', 'KEUANGAN_FAK', 'KEUANGAN_PRODI')")
public class InvoiceKomponenMahasiswaController {

    private final InvoiceKomponenService service;

    @Operation(summary = "Add Invoice Komponen Mahasiswa")
    @PostMapping
    public ResponseEntity<ApiResDto<InvoiceKomponenResDto>> save(
            @Valid @RequestBody InvoiceKomponenMahasiswaReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.create(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<InvoiceKomponenResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.CREATED.getMessage())
                            .build()

            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Invoice Komponen Mahasiswa By Pagination")
    @GetMapping
    public ResponseEntity<ApiResDto<List<InvoiceKomponenResDto>>> getAllKomponen(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<InvoiceKomponenResDto> paginated = service.getPaginated(keyword, page, size);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<InvoiceKomponenResDto>>builder()
                            .data(paginated.getContent())
                            .pagination(PaginationDto.fromPage(paginated))
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Update Invoice Komponen Mahasiswa")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResDto<InvoiceKomponenResDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody InvoiceKomponenMahasiswaReqDto request,
            HttpServletRequest servletRequest
            ) {
        try {
            service.update(id, request, servletRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<InvoiceKomponenResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATED.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Delete invoice komponen Mahasiswa")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<InvoiceKomponenResDto>> delete(
            @PathVariable UUID id,
            HttpServletRequest servletRequest
    ){
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<InvoiceKomponenResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATED.getMessage())
                            .build());
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
