package com.siakad.controller.akademik;

import com.siakad.dto.request.KelasKuliahReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.KelasKuliahResDto;
import com.siakad.dto.response.PaginationDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.KelasKuliahService;
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

@Tag(name = "Kelas Kuliah")
@RestController
@RequestMapping("/akademik/kelas-kuliah")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AKADEMIK_UNIV')")
public class KelasKuliahController {

    private final KelasKuliahService service;

    @Operation(summary = "Add Kelas Kuliah")
    @PostMapping
    public ResponseEntity<ApiResDto<KelasKuliahResDto>> save(
            @Valid @RequestBody KelasKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.create(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KelasKuliahResDto>builder()
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

    @Operation(summary = "Get One Kelas Kuliah")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<KelasKuliahResDto>> getOne(@PathVariable UUID id) {
        try {
            KelasKuliahResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<KelasKuliahResDto>builder()
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

    @Operation(summary = "Get Kelas Kuliah By Pagination")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<KelasKuliahResDto>>> getPaginated(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String periodeAkademik,
            @RequestParam(required = false) String tahunKuriKulum,
            @RequestParam(required = false) String programStudi,
            @RequestParam(required = false) String sistemKuliah,
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

            Page<KelasKuliahResDto> search = service.search(keyword, periodeAkademik, tahunKuriKulum, programStudi, sistemKuliah, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<KelasKuliahResDto>>builder()
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

    @Operation(summary = "Delete Kelas Kuliah")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<KelasKuliahResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<KelasKuliahResDto>builder()
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
