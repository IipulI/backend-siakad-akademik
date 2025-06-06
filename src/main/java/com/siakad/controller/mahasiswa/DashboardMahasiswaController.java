package com.siakad.controller.mahasiswa;

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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Dashboard Mahasiswa")
@RestController
@RequestMapping("/mahasiswa/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MAHASISWA')")
public class DashboardMahasiswaController {

    private final DashboardService service;

    @Operation(summary = "Get Tagihan")
    @GetMapping("/tagihan")
    public ResponseEntity<ApiResDto<TagihanMahasiswaDto>> getTagihan() {
        try {
            TagihanMahasiswaDto tagihanMahasiswaDto = service.getTagihanMahasiswa();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<TagihanMahasiswaDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(tagihanMahasiswaDto)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Grafik Akademik")
    @GetMapping("/grafik-akademik")
    public ResponseEntity<ApiResDto<GrafikAkademikDto>> getGrafikAkademik() {
        try {
            GrafikAkademikDto grafikAkademik = service.getGrafikAkademik();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<GrafikAkademikDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(grafikAkademik)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Tagihan Komponen")
    @GetMapping("/tagihan-komponen")
    public ResponseEntity<ApiResDto<TagihanKomponenMahasiswaDto>> getTagihanKomponen() {
        try {
            TagihanKomponenMahasiswaDto tagihanKomponenMahasiswa = service.getTagihanKomponenMahasiswa();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<TagihanKomponenMahasiswaDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(tagihanKomponenMahasiswa)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
