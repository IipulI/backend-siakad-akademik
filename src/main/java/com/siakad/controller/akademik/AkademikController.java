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
    private final AkademikService akademikService;
    private final KrsService krsService;
    private final MataKuliahService mataKuliahService;

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

    @Operation(summary = "Get manajemen OBE")
    @GetMapping("/manajemen-obe")
    public ResponseEntity<ApiResDto<List<ManajemenOBEResDto>>> getStatusOverview(
            @RequestParam(required = false) String tahunKurikulum,
            @RequestParam(required = false) String programStudi,
            @RequestParam(required = false) String jenjang
    ) {
        try {
            List<ManajemenOBEResDto> result = akademikService.getStatusOverview(
                    tahunKurikulum,
                    programStudi,
                    jenjang
            );

            return ResponseEntity.ok(
                    ApiResDto.<List<ManajemenOBEResDto>>builder()
                            .status("SUCCESS")
                            .message("Data status overview program studi berhasil diambil.")
                            .data(result)
                            .build()
            );
        }
        catch (ApplicationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Operation(summary = "Get one manajemen obe")
    @GetMapping("/manajemen-obe/{id}")
    public ResponseEntity<ApiResDto<ManajemenOBEResDto>> getOneStatusOverview(
            @PathVariable UUID id,
            @RequestParam(required = false) String tahunKurikulum
    ){
        try {
            ManajemenOBEResDto result = akademikService.getOneStatusOverview(id, tahunKurikulum);

            return ResponseEntity.ok(
                    ApiResDto.<ManajemenOBEResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message("Data satu status overview program studi berhasil diambil")
                            .data(result)
                            .build()
            );
        }
        catch (ApplicationException e){
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Riwayat Krs by Mahasiswa ID")
    @GetMapping("/riwayat-krs/{mahasiswaId}")
    public ResponseEntity<ApiResDto<RiwayatKrsDto>> getRiwayatKrs  (
            @PathVariable("mahasiswaId") UUID id,
            @RequestParam("namaPeriode") String namaPeriode
    ) {
        try {
            RiwayatKrsDto riwayatKrs = krsService.getRiwayatKrs(id, namaPeriode);
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

    @Operation(summary = "Get MataKuliahCplCpmk")
    @GetMapping("/cpl-cpmk/{mataKuliahId}")
    public ResponseEntity<ApiResDto<MataKuliahCplCpmkResDto>> getRiwayatKrs  (
            @PathVariable("mataKuliahId") UUID id
    ) {
        try {

            MataKuliahCplCpmkResDto mataKuliahCplCpmk = mataKuliahService.getMataKuliahCplCpmk(id);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<MataKuliahCplCpmkResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(mataKuliahCplCpmk)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}


// TODO : DOSEN MATA KULIAH (tinggal cpl cpmk)
// TODO : DOSEN PEMBIMIBING AKADEMIK (tinggal detail krs mahasiswa)
// TODO : DOSEN JADWAL TAMBAH PARAM TANGGAL

// DONE
// TODO : Akademik mahasiswa kasih param
// TODO : ALL get active periode akademik
// TODO : DOSEN & AKADEMIK, PEMBIMIBING AKADEMIK GANTI UUID JADI STRING
// TODO : PENGUMUMAN GET ALL

