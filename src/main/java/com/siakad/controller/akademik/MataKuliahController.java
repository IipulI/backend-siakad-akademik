package com.siakad.controller.akademik;

import com.siakad.dto.request.KurikulumProdiReqDto;
import com.siakad.dto.request.MataKuliahReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.KurikulumProdiResDto;
import com.siakad.dto.response.MataKuliahResDto;
import com.siakad.dto.response.PaginationDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.MataKuliahService;
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
import java.util.UUID;

@Tag(name = "Mata Kuliah")
@RestController
@RequestMapping("/akademik")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('KEUANGAN_UNIV', 'KEUANGAN_FAK', 'KEUANGAN_PRODI')")
public class MataKuliahController {

    private final MataKuliahService service;

    @Operation(summary = "Add Mata Kuliah")
    @PostMapping("/mata-kuliah")
    public ResponseEntity<ApiResDto<MataKuliahResDto>> save(
            @Valid @RequestBody MataKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.create(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<MataKuliahResDto>builder()
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

    @Operation(summary = "Get One Mata Kuliah")
    @GetMapping("/mata-kuliah/{id}")
    public ResponseEntity<ApiResDto<MataKuliahResDto>> getOne(@PathVariable UUID id) {
        try {
            MataKuliahResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<MataKuliahResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(one)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Mata Kuliah By Pagiantion")
    @GetMapping("/mata-kuliah")
    public ResponseEntity<ApiResDto<List<MataKuliahResDto>>> getPaginated(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String programStudi,
            @RequestParam(required = false) String jenisMataKuliah,
            @RequestParam(required = false) String tahunKurikulum,
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

            Page<MataKuliahResDto> search = service.search(keyword, programStudi, jenisMataKuliah, tahunKurikulum, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<MataKuliahResDto>>builder()
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

    @Operation(summary = "Update Mata Kuliah")
    @PutMapping("/mata-kuliah/{id}")
    public ResponseEntity<ApiResDto<MataKuliahResDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody MataKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.update(request, id, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<MataKuliahResDto>builder()
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

    @Operation(summary = "Delete Mata Kuliah")
    @DeleteMapping("/mata-kuliah/{id}")
    public ResponseEntity<ApiResDto<MataKuliahResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<MataKuliahResDto>builder()
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

    @Operation(summary = "Add Kurikulum Prodi")
    @PutMapping("/kurikulum-prodi/{id}")
    public ResponseEntity<ApiResDto<MataKuliahResDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody KurikulumProdiReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.updateKurikulum(id, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<MataKuliahResDto>builder()
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

    @Operation(summary = "Get Kurikulum Prodi ")
    @GetMapping("/kurikulum-prodi")
    public ResponseEntity<ApiResDto<List<KurikulumProdiResDto>>> getAll(
            @RequestParam String programStudi,
            @RequestParam String tahunKurikulum
    ) {
        try {
            List<KurikulumProdiResDto> kurikulum = service.getKurikulumPerSemester(programStudi, tahunKurikulum);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<KurikulumProdiResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(kurikulum)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
