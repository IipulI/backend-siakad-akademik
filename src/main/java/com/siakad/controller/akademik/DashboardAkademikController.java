package com.siakad.controller.akademik;

import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Dashboard Akademik")
@RestController
@RequestMapping("/akademik/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('AKADEMIK_UNIV', 'AKADEMIK_FAK', 'AKADEMIK_PRODI')")
public class DashboardAkademikController {

    private final DashboardService service;

    @Operation(summary = "Get Card")
    @GetMapping("/card")
    public ResponseEntity<ApiResDto<CardDto>> getCard() {
        try {
            CardDto card = service.getCard();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<CardDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(card)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Akm Angkatan")
    @GetMapping("/akm-angkatan")
    public ResponseEntity<ApiResDto<List<AkmAngkatanDto>>> getAkmAngkatan() {
        try {
            List<AkmAngkatanDto> akmAngkatan = service.getAkmAngkatan();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<AkmAngkatanDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(akmAngkatan)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Akm Prodi")
    @GetMapping("/akm-prodi")
    public ResponseEntity<ApiResDto<List<AkmProdiDto>>> getAkmProdi() {
        try {
            List<AkmProdiDto> akmProdi = service.getJumlahMahasiswaPerProdi();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<AkmProdiDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(akmProdi)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Mahasiswa Baru")
    @GetMapping("/mahasiswa-baru")
    public ResponseEntity<ApiResDto<List<MahasiswaBaruDto>>> getMahasiswaBaru() {
        try {
            List<MahasiswaBaruDto> mahasiswaBaru = service.getMahasiswaBaru();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<MahasiswaBaruDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(mahasiswaBaru)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
