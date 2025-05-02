package com.siakad.controller.akademik;

import com.siakad.dto.request.KomponenPenilaianReqDto;
import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.KomponenPenilaianResDto;
import com.siakad.dto.response.MahasiswaResDto;
import com.siakad.dto.response.PaginationDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.KomponenPenilaianService;
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

@Tag(name = "Komponen Penilaian")
@RestController
@RequestMapping("/akademik/komponen-penilaian")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AKADEMIK_UNIV')")
public class KomponenPenilaianController {

    private final KomponenPenilaianService service;


    @Operation(summary = "Add Komponen Penilaian")
    @PostMapping
    public ResponseEntity<ApiResDto<KomponenPenilaianResDto>> save(
            @Valid @RequestBody KomponenPenilaianReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.create(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KomponenPenilaianResDto>builder()
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

    @Operation(summary = "Get One Komponen Penilaian")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<KomponenPenilaianResDto>> getOne(@PathVariable UUID id) {
        try {
            KomponenPenilaianResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<KomponenPenilaianResDto>builder()
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

    @Operation(summary = "Get Komponen Penilain By Pagiantion")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<KomponenPenilaianResDto>>> getPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<KomponenPenilaianResDto> paginated = service.getPaginated(page, size);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<KomponenPenilaianResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(paginated.getContent())
                            .pagination(PaginationDto.fromPage(paginated))
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Update Komponen Penilaian")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResDto<KomponenPenilaianResDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody KomponenPenilaianReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.update(id, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KomponenPenilaianResDto>builder()
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


    @Operation(summary = "Delete Komponen Penilaian")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<KomponenPenilaianResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KomponenPenilaianResDto>builder()
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
