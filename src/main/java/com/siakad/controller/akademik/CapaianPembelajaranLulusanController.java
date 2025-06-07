package com.siakad.controller.akademik;

import com.siakad.dto.request.CapaianPembelajaranLulusanReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.CapaianPembelajaranLulusanResDto;
import com.siakad.dto.response.PaginationDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.CapaianPembelajaranLulusanService;
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

@Tag(name = "Capaian Pembelajaran Lulusan")
@RestController
@RequestMapping("/akademik/capaian-pembelajaran-lulusan")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('AKADEMIK_UNIV', 'AKADEMIK_FAK', 'AKADEMIK_PRODI')")
public class CapaianPembelajaranLulusanController {

    private final CapaianPembelajaranLulusanService service;

    @Operation(summary = "Add Capaian Pembelajaran Lulusan")
    @PostMapping
    public ResponseEntity<ApiResDto<CapaianPembelajaranLulusanResDto>> save(
            @Valid @RequestBody CapaianPembelajaranLulusanReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.save(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<CapaianPembelajaranLulusanResDto>builder()
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

    @Operation(summary = "Get One Capaian Pembelajaran Lulusan")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<CapaianPembelajaranLulusanResDto>> getOne(@PathVariable UUID id) {
        try {
            CapaianPembelajaranLulusanResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<CapaianPembelajaranLulusanResDto>builder()
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

    @Operation(summary = "Get Capaian Pembelajaran Lulusan By Pagiantion")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<CapaianPembelajaranLulusanResDto>>> getPaginated(
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

            Page<CapaianPembelajaranLulusanResDto> search = service.getPaginate(tahunKurikulum, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<CapaianPembelajaranLulusanResDto>>builder()
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

    @Operation(summary = "Update Capaian Pembelajaran Lulusan")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResDto<CapaianPembelajaranLulusanResDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CapaianPembelajaranLulusanReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.update(id, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<CapaianPembelajaranLulusanResDto>builder()
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

    @Operation(summary = "Delete Capaian Pembelajaran Lulusan")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<CapaianPembelajaranLulusanResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<CapaianPembelajaranLulusanResDto>builder()
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

}
