package com.siakad.controller.akademik;

import com.siakad.dto.request.PeriodeAkademikReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.PaginationDto;
import com.siakad.dto.response.PeriodeAkademikResDto;
import com.siakad.dto.response.PeriodeDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.PeriodeAkademikService;
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

@Tag(name = "Periode Akademik")
@RestController
@RequestMapping("/akademik/periode-akademik")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('KEUANGAN_UNIV', 'KEUANGAN_FAK', 'KEUANGAN_PRODI')")
public class PeriodeAkademikController {

    private final PeriodeAkademikService service;

    @Operation(summary = "Add Periode Akademik")
    @PostMapping
    public ResponseEntity<ApiResDto<PeriodeAkademikResDto>> save(
            @Valid @RequestBody PeriodeAkademikReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.create(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<PeriodeAkademikResDto>builder()
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

    @Operation(summary = "Get One Periode Akademik")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<PeriodeAkademikResDto>> getOne(@PathVariable UUID id) {
        try {
            PeriodeAkademikResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<PeriodeAkademikResDto>builder()
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

    @Operation(summary = "Get Periode Akademik By Pagiantion")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<PeriodeAkademikResDto>>> getPaginated(
            @RequestParam(required = false) String keyword,
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

            Page<PeriodeAkademikResDto> data = service.search(keyword, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<PeriodeAkademikResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(data.getContent())
                            .pagination(PaginationDto.fromPage(data))
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR,
                    "Failed to get periode akademik");
        }
    }

    @Operation(summary = "Update Periode Akademik")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResDto<PeriodeAkademikResDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody PeriodeAkademikReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.update(id, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<PeriodeAkademikResDto>builder()
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

    @Operation(summary = "Delete Periode akademik")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<PeriodeAkademikResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<PeriodeAkademikResDto>builder()
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

    @Operation(summary = "Get All Periode Akademik for dropdown")
    @GetMapping("/dropdown")
    public ResponseEntity<ApiResDto<List<PeriodeDto>>> getAllDropdown() {
        try {
            List<PeriodeDto> all = service.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<PeriodeDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(all)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Set Periode Akademik from Planed to Active")
    @PutMapping("/{id}/change-status")
    public ResponseEntity<ApiResDto<Objects>> changeStatus(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.changeStatus(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<Objects>builder()
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

}
