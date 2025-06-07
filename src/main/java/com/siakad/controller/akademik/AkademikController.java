package com.siakad.controller.akademik;


import com.siakad.dto.response.*;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Tag(name = "Akademik")
@RestController
@RequestMapping("/akademik")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('AKADEMIK_UNIV', 'AKADEMIK_FAK', 'AKADEMIK_PRODI')")
public class AkademikController {

    private final KelasKuliahService service;
    private final HasilStudiService hasilStudiService;
    private final KrsService krsService;
    private final DashboardService dashboardService;

    @Operation(summary = "Ganti semester")
    @PutMapping("/ganti-semester")
    public ResponseEntity<ApiResDto<Objects>> gantiSemester(){
        try {

            service.gantiSemester();

            return ResponseEntity.ok(
                    ApiResDto.<Objects>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Transkip Nilai")
    @GetMapping("/transkip/{mahasiswaId}")
    public ResponseEntity<ApiResDto<TranskipDto>> getTranskip(@PathVariable UUID mahasiswaId) {
        try {
            List<KrsRincianMahasiswa> rincianMahasiswa = hasilStudiService.getRincianMahasiswa(mahasiswaId);

            TranskipDto transkipDto = hasilStudiService.buildTranskip(rincianMahasiswa);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<TranskipDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATED.getMessage())
                            .data(transkipDto)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Khs")
    @GetMapping("/khs")
    public ResponseEntity<ApiResDto<HasilStudiDto>> getTranskip(@RequestParam UUID mahasiswaId, @RequestParam UUID periodeAkademikId) {
        try {
            HasilStudiDto hasilStudi = hasilStudiService.getHasilStudi(mahasiswaId, periodeAkademikId);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<HasilStudiDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATED.getMessage())
                            .data(hasilStudi)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get mengulang by Periode")
    @GetMapping("/mengulang")
    public ResponseEntity<ApiResDto<List<MengulangResDto>>> krsMengulang(
            @RequestParam UUID mahasiswaId,
            @RequestParam UUID periodeAkademikId
    ) {
        try {
            List<MengulangResDto> allMengulangByPeriode = krsService.getAllMengulangByPeriode(mahasiswaId, periodeAkademikId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<MengulangResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(allMengulangByPeriode)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get All Finalisasi MK")
    @GetMapping("/finalisasi-mk")
    public ResponseEntity<ApiResDto<List<FinalisasiMkDto>>> finalisasiMk    (
            @RequestParam UUID mahasiswaId
    ) {
        try {
            List<FinalisasiMkDto> allFinalisasiMk = krsService.getAllFinalisasiMk(mahasiswaId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<FinalisasiMkDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(allFinalisasiMk)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get All Status Semester")
    @GetMapping("/status-semester")
    public ResponseEntity<ApiResDto<List<StatusSemesterDto>>> getAllStatusSemester  (
            @RequestParam UUID mahasiswaId
    ) {
        try {
            List<StatusSemesterDto> statusSemester = krsService.getStatusSemester(mahasiswaId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<StatusSemesterDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(statusSemester)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Riwayat Krs by Mahasiswa ID")
    @GetMapping("/riwayat-krs/{id}")
    public ResponseEntity<ApiResDto<RiwayatKrsDto>> getRiwayatKrs  (
            @PathVariable UUID id) {
        try {
            RiwayatKrsDto riwayatKrs = krsService.getRiwayatKrs(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<RiwayatKrsDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(riwayatKrs)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Delete Krs")
    @DeleteMapping("/krs/{id}")
    public ResponseEntity<ApiResDto<Objects>> deletedKrs(@PathVariable UUID id, HttpServletRequest request) {
        {
            try {
                krsService.deleteKrs(id, request);
                return ResponseEntity.status(HttpStatus.OK).body(
                        ApiResDto.<Objects>builder()
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

    @Operation(summary = "Get Mahasiswa Baru")
    @GetMapping("/tagihan-komponen/{id}")
    public ResponseEntity<ApiResDto<TagihanKomponenMahasiswaDto>> getMahasiswaBaru(@PathVariable UUID id) {
        try {

            TagihanKomponenMahasiswaDto tagihanKomponenMahasiswa = dashboardService.getTagihanKomponenMahasiswa(id);
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
