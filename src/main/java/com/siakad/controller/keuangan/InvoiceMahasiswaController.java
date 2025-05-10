package com.siakad.controller.keuangan;

import com.siakad.dto.request.InvoiceMahasiswaReqDto;
import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.InvoiceMahasiwaService;
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

@Tag(name = "Invoice Mahasiswa")
@RestController
@RequestMapping("/keuangan/invoice-mahasiswa")
@RequiredArgsConstructor
@PreAuthorize("hasRole('KEUANGAN_UNIV')")
public class InvoiceMahasiswaController {

    private final InvoiceMahasiwaService service;


    @Operation(summary = "Add Invoice Mahasiswa")
    @PostMapping
    public ResponseEntity<ApiResDto<InvoiceMahasiswaResDto>> save(
            @Valid @RequestBody InvoiceMahasiswaReqDto request,
            HttpServletRequest servletRequest
            ) {
        try {
            service.create(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<InvoiceMahasiswaResDto>builder()
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


    @Operation(summary = "Get Paginate Mapping Mahasiswa")
    @GetMapping("/mahasiswa")
    public ResponseEntity<ApiResDto<List<MahasiswaKeuanganResDto>>> getPaginateMahasiswa(
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int size
    ) {
        try {

            Page<MahasiswaKeuanganResDto> paginateMahasiswa = service.getPaginateMahasiswa(page, size);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<MahasiswaKeuanganResDto>>builder()
                            .data(paginateMahasiswa.getContent())
                            .pagination(PaginationDto.fromPage(paginateMahasiswa))
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


    @Operation(summary = "Get Data Tagihan Terbaru")
    @GetMapping("/tagihan-mahasiswa")
    public ResponseEntity<ApiResDto<List<TagihanMahasiswaResDto>>> getPaginateTagihanMahasiswa(
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int size
    ) {
        try {

            Page<TagihanMahasiswaResDto> paginateTagihanMahasiswa = service.getPaginateTagihanMahasiswa(page, size);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<TagihanMahasiswaResDto>>builder()
                            .data(paginateTagihanMahasiswa.getContent())
                            .pagination(PaginationDto.fromPage(paginateTagihanMahasiswa))
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


    @Operation(summary = "Get Tagihan Ringkasan Mahasiswa untuk dashboard")
    @GetMapping("/tagihan-mahasiswa/ringkasan")
    public ResponseEntity<ApiResDto<RingkasanTagihanResDto>> getTagihanRingkasianMahasiswa() {
        try {

            var data = service.getRingkasanTagihan();

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<RingkasanTagihanResDto>builder()
                            .data(data)
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
}
