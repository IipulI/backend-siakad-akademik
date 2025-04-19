package com.siakad.controller;

import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.MahasiswaResDto;
import com.siakad.dto.response.PaginationDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.MahasiswaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

@Tag(name = "Mahasiswa")
@RestController
@SecurityRequirement(name = "Bearer")
@RequestMapping("/mahasiswa")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AKADEMIK_UNIV')")
public class MahasiswaController {

    private final MahasiswaService service;


    @Operation(summary = "Add Mahasiswa")
    @PostMapping
    public ResponseEntity<ApiResDto<MahasiswaResDto>> save(
            @Valid @RequestBody MahasiswaReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.create(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<MahasiswaResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.CREATED.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionType.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    @Operation(summary = "Get One Mahasiswa")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<MahasiswaResDto>> getOne(@PathVariable UUID id) {
        try {
            MahasiswaResDto mahasiswaResDto = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<MahasiswaResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(mahasiswaResDto)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionType.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    @Operation(summary = "Get Mahasiswa By Pagiantion")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<MahasiswaResDto>>> getPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<MahasiswaResDto> paginated = service.getPaginated(page, size);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<MahasiswaResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(paginated.getContent())
                            .pagination(PaginationDto.fromPage(paginated))
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionType.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    @Operation(summary = "Update Mahasiswa")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResDto<MahasiswaResDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody MahasiswaReqDto request,
            HttpServletRequest servletRequest
            ) {
        try {
            service.update(id, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<MahasiswaResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATED.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionType.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    @Operation(summary = "Delete Mahasiswa")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<MahasiswaResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<MahasiswaResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.DELETED.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    ExceptionType.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

}
