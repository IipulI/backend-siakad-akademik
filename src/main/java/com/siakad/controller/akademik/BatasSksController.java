package com.siakad.controller.akademik;

import com.siakad.dto.request.BatasSksReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.BatasSksResDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.BatasSksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Batas SKS")
@RestController
@RequestMapping("/akademik/batas-sks")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('AKADEMIK_UNIV', 'AKADEMIK_FAK', 'AKADEMIK_PRODI')")
public class BatasSksController {

    private final BatasSksService service;

    @Operation(summary = "Add Batas SKS")
    @PostMapping
    public ResponseEntity<ApiResDto<BatasSksResDto>> save(
            @Valid @RequestBody BatasSksReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.save(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<BatasSksResDto>builder()
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

    @Operation(summary = "Get One Batas SKS")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<BatasSksResDto>> getOne(@PathVariable UUID id) {
        try {
            BatasSksResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<BatasSksResDto>builder()
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

    @Operation(summary = "Get All Batas SKS")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<BatasSksResDto>>> getAll() {
        try {
            List<BatasSksResDto> all = service.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<BatasSksResDto>>builder()
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

    @Operation(summary = "Update Batas SKS")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResDto<BatasSksResDto>> save(
            @PathVariable UUID id,
            @Valid @RequestBody BatasSksReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.update(id, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<BatasSksResDto>builder()
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

    @Operation(summary = "Delete Batas SKS")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<BatasSksResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<BatasSksResDto>builder()
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
