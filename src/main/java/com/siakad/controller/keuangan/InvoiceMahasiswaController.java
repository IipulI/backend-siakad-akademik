package com.siakad.controller.keuangan;

import com.siakad.dto.request.InvoiceMahasiswaReqDto;
import com.siakad.dto.request.TandaiLunasReqDto;
import com.siakad.dto.request.TanggalTenggatReqDto;
import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.InvoiceMahasiwaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Tag(name = "Invoice Mahasiswa")
@RestController
@RequestMapping("/keuangan/invoice-mahasiswa")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('KEUANGAN_UNIV', 'KEUANGAN_FAK', 'KEUANGAN_PRODI')")
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


    @Operation(summary = "Get Mahasiswa")
    @GetMapping("/mahasiswa")
    public ResponseEntity<ApiResDto<List<MahasiswaKeuanganResDto>>> getPaginated(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fakultas,
            @RequestParam(required = false) String angkatan,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String programStudi,
            @RequestParam(required = false) String npm,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        try {
            // Parse sort parameter
            String[] sortParams = sort.split(",");
            Sort.Direction direction = sortParams.length > 1 ?
                    Sort.Direction.fromString(sortParams[1]) : Sort.Direction.DESC;
            Sort sortObj = Sort.by(direction, sortParams[0]);

            Pageable pageable = PageRequest.of(page - 1, size, sortObj); // page dikurangi 1 karena UI biasanya mulai dari 1

            Page<MahasiswaKeuanganResDto> search = service.getPaginateMahasiswa(keyword, fakultas, angkatan, semester, programStudi, npm, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<MahasiswaKeuanganResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(search.getContent())
                            .pagination(PaginationDto.fromPage(search))
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @Operation(summary = "Get Mahasiswa")
    @GetMapping("/tagihan-mahasiswa")
    public ResponseEntity<ApiResDto<List<TagihanMahasiswaResDto>>> getPaginated(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String npm,
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String angkatan,
            @RequestParam(required = false) String programStudi,
            @RequestParam(required = false) String fakultas,
            @RequestParam(required = false) String periodeAkademik,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        try {
            // Parse sort parameter
            String[] sortParams = sort.split(",");
            Sort.Direction direction = sortParams.length > 1 ?
                    Sort.Direction.fromString(sortParams[1]) : Sort.Direction.DESC;
            Sort sortObj = Sort.by(direction, sortParams[0]);

            Pageable pageable = PageRequest.of(page - 1, size, sortObj);

            Page<TagihanMahasiswaResDto> paginateTagihanMahasiswa = service.getPaginateTagihanMahasiswa(keyword, npm, nama, semester, angkatan, programStudi, fakultas, periodeAkademik, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<TagihanMahasiswaResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(paginateTagihanMahasiswa.getContent())
                            .pagination(PaginationDto.fromPage(paginateTagihanMahasiswa))
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get One Data Tagihan Terbaru")
    @GetMapping("/tagihan-mahasiswa/{id}")
    public ResponseEntity<ApiResDto<TagihanMahasiswaResDto>> getOneTagihanMahasiswa(
            @PathVariable UUID id
    ) {
        try {

            TagihanMahasiswaResDto one = service.getOne(id);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<TagihanMahasiswaResDto>builder()
                            .data(one)
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

    @Operation(summary = "Delete Tagihan Terbaru")
    @DeleteMapping("/tagihan-mahasiswa/{id}")
    public ResponseEntity<ApiResDto<TagihanMahasiswaResDto>> deleteTagihanMahasiswa(
            @PathVariable UUID id,
            HttpServletRequest servletRequest
    ) {
        try {

            service.deleteInvoiceMahasiswa(id, servletRequest);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<TagihanMahasiswaResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.DELETED.getMessage())
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

    @Operation(summary = "Update tanggal tenggat tagihan mahasiswa")
    @PutMapping("/{id}/tanggal-tenggat")
    public ResponseEntity<ApiResDto<Objects>> updateTanggalTenggat(
            @PathVariable UUID id,
            @Valid @RequestBody TanggalTenggatReqDto reqDto,
            HttpServletRequest servletRequest
            ) {
        try {

            service.updateTanggalTenggatTagihan(id, reqDto, servletRequest);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<Objects>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATE_INVOICE_MAHASISWA.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Tandai lunas")
    @PutMapping("/tandai-lunas")
    public ResponseEntity<ApiResDto<Objects>> tandaiLunas(
            @Valid @RequestBody TandaiLunasReqDto reqDto,
            HttpServletRequest servletRequest
    ) {
        try {

            service.tandaiLunas(reqDto, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<Objects>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATE_INVOICE_MAHASISWA.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Data Tagihan Lunas")
    @GetMapping("/tagihan-lunas")
    public ResponseEntity<ApiResDto<List<TransaksiLunasDto>>> getTagihanLunas() {
        try {

            List<TransaksiLunasDto> allTagihanLunas = service.getAllTagihanLunas();

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<TransaksiLunasDto>>builder()
                            .data(allTagihanLunas)
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

    @Operation(summary = "Get Data Statistik Fakultas")
    @GetMapping("/tagihan-fakultas")
    public ResponseEntity<ApiResDto<List<StatistikTagihanFakultasDto>>> getStatistikTagihanFakultas() {
        try {

            List<StatistikTagihanFakultasDto> allStatikTagihanFakultas = service.getAllStatikTagihanFakultas();

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<StatistikTagihanFakultasDto>>builder()
                            .data(allStatikTagihanFakultas)
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
