package com.siakad.controller.akademik;

import com.siakad.dto.request.KomposisiPenilaianReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.KomposisiPenilaianResDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.KomposisiPenilaianService;
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

@Tag(name = "Komposisi Penilaian")
@RestController
@RequestMapping("/akademik/komposisi-nilai")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('KEUANGAN_UNIV', 'KEUANGAN_FAK', 'KEUANGAN_PRODI')")
public class KomposisiPenilaianController {

    private final KomposisiPenilaianService service;

    @Operation(summary = "Add Komposisi nilai")
    @PostMapping
    public ResponseEntity<ApiResDto<KomposisiPenilaianResDto>> save(
            @Valid @RequestBody KomposisiPenilaianReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.save(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KomposisiPenilaianResDto>builder()
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

    @Operation(summary = "Update Komposisi nilai")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResDto<KomposisiPenilaianResDto>> save(
            @PathVariable UUID id,
            @Valid @RequestBody KomposisiPenilaianReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.update(request, id, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KomposisiPenilaianResDto>builder()
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

    @Operation(summary = "Get One Komposisi nilai")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<KomposisiPenilaianResDto>> getOne(@PathVariable UUID id) {
        try {
            KomposisiPenilaianResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<KomposisiPenilaianResDto>builder()
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

    @Operation(summary = "Get All Komposisi nilai")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<KomposisiPenilaianResDto>>> getAll() {
        try {
            List<KomposisiPenilaianResDto> all = service.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<KomposisiPenilaianResDto>>builder()
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

    @Operation(summary = "Delete Komposisi nilai")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<KomposisiPenilaianResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<KomposisiPenilaianResDto>builder()
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
