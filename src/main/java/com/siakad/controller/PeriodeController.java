package com.siakad.controller;

import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.PeriodeAkademikResDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.PeriodeAkademikService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Periode Akademik")
@RestController
@RequestMapping("/periode-akademik")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('KEUANGAN_UNIV', 'KEUANGAN_FAK', 'KEUANGAN_PRODI','AKADEMIK_UNIV', 'AKADEMIK_FAK', 'AKADEMIK_PRODI', 'DOSEN', 'MAHASISWA' )")
public class PeriodeController {

    private PeriodeAkademikService service;

    @Operation(summary = "Get Periode Akademik Active status")
    @GetMapping
    public ResponseEntity<ApiResDto<PeriodeAkademikResDto>> get() {
        try {
            PeriodeAkademikResDto periodeActive = service.getPeriodeActive();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<PeriodeAkademikResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .data(periodeActive)
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
