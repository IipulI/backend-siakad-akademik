package com.siakad.controller.akademik;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siakad.dto.request.KeluargaMahasiswaReqDto;
import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.*;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Tag(name = "Mahasiswa")
@RestController
@RequestMapping("/akademik/mahasiswa")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('AKADEMIK_UNIV', 'AKADEMIK_FAK', 'AKADEMIK_PRODI')")
public class MahasiswaController {

    private final MahasiswaService service;
    private final KeluargaMahasiswaService keluargaService;
    private final ObjectMapper objectMapper;
    private final KelasKuliahService kelasKuliahService;
    private final HasilStudiService hasilStudiService;
    private final KrsService krsService;
    private final DashboardService dashboardService;
    private final KomposisiNilaiMataKuliahMhsService komposisiNilaiMataKuliahMhsService;


    @Operation(summary = "Add Mahasiswa")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResDto<MahasiswaResDto>> save(
            @RequestPart(value = "fotoProfil", required = false) MultipartFile fotoProfil,
            @RequestPart(value = "ijazahSekolah", required = false) MultipartFile ijazahSekolah,
            @RequestPart("request") String requestJson,
            HttpServletRequest servletRequest) {

        try {
            MahasiswaReqDto request = objectMapper.readValue(requestJson, MahasiswaReqDto.class);
            service.create(request, fotoProfil, ijazahSekolah, servletRequest);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResDto.<MahasiswaResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.CREATED.getMessage())
                            .build());
        } catch (JsonProcessingException e) {
            throw new ApplicationException(ExceptionType.BAD_REQUEST, "Invalid JSON format in 'request'");
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @Operation(summary = "Get Foto Profil")
    @GetMapping("/{id}/foto-profil")
    public ResponseEntity<byte[]> getFotoProfil(@PathVariable UUID id) {
        try {
            byte[] fotoProfil = service.getFotoProfil(id);
            if (fotoProfil == null || fotoProfil.length == 0) {
                throw new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND);
            }
            Tika tika = new Tika();
            String mimeType = tika.detect(fotoProfil);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));

            return new ResponseEntity<>(fotoProfil, headers, HttpStatus.OK);

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Ijazah Sekolah")
    @GetMapping("/{id}/ijazah-sekolah")
    public ResponseEntity<byte[]> getIjazahSekolah(@PathVariable UUID id) {
        try {
            byte[] ijazahSekolah = service.getIjazahSekolah(id);
            if (ijazahSekolah == null || ijazahSekolah.length == 0) {
                throw new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND);
            }
            Tika tika = new Tika();
            String mimeType = tika.detect(ijazahSekolah);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));
            return new ResponseEntity<>(ijazahSekolah, headers, HttpStatus.OK);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
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
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Mahasiswa By Pagiantion")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<MahasiswaResDto>>> getPaginated(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String programStudi,
            @RequestParam(required = false) String jenisPendaftaran,
            @RequestParam(required = false) String kelasPerkuliahan,
            @RequestParam(required = false) String angkatan,
            @RequestParam(required = false) String jalurPendaftaran,
            @RequestParam(required = false) String statusMahasiswa,
            @RequestParam(required = false) String gelombang,
            @RequestParam(required = false) String jenisKelamin,
            @RequestParam(required = false) String sistemKuliah,
            @RequestParam(required = false) String kurikulum,
            @RequestParam(required = false) String periodeMasuk,
            @RequestParam(required = false) String periodeKeluar,
            @RequestParam(required = false) Integer pagination,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<MahasiswaResDto> paginated = service.getPaginated(
                    keyword,
                    programStudi,
                    jenisPendaftaran,
                    kelasPerkuliahan,
                    angkatan,
                    jalurPendaftaran,
                    statusMahasiswa,
                    gelombang,
                    jenisKelamin,
                    sistemKuliah,
                    kurikulum,
                    periodeMasuk,
                    periodeKeluar,
                    page, size);

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
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Update Mahasiswa")
    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResDto<MahasiswaResDto>> update(
            @PathVariable UUID id,
            @RequestPart(value = "fotoProfil", required = false) MultipartFile fotoProfil,
            @RequestPart(value = "ijazahSekolah", required = false) MultipartFile ijazahSekolah,
            @RequestPart("request") String requestJson,
            HttpServletRequest servletRequest
            ) {
        try {
            MahasiswaReqDto request = objectMapper.readValue(requestJson, MahasiswaReqDto.class);
            service.update(id,fotoProfil, ijazahSekolah, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<MahasiswaResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATED.getMessage())
                            .build()
            );
        } catch (JsonProcessingException e) {
            throw new ApplicationException(ExceptionType.BAD_REQUEST, "Invalid JSON format in 'request'");
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Delete Mahasiswa")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<MahasiswaResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<MahasiswaResDto>builder()
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

    @Operation(summary = "Add Keluarga Mahasiswa")
    @PostMapping("/{id}/keluarga-mahasiswa")
    public ResponseEntity<ApiResDto<KeluargaMahasiswaResDto>> save(
            @PathVariable UUID id,
            @Valid @RequestBody KeluargaMahasiswaReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            keluargaService.create(id, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KeluargaMahasiswaResDto>builder()
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

    @Operation(summary = "Get One Keluarga Mahasiswa")
    @GetMapping("/{id}/keluaga-mahasiswa/{keluargaId}")
    public ResponseEntity<ApiResDto<KeluargaMahasiswaResDto>> getOne(
            @PathVariable UUID id,
            @PathVariable UUID keluargaId) {
        try {
            var data = keluargaService.getOne(id, keluargaId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<KeluargaMahasiswaResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(data)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Update Keluarga Mahasiswa")
    @PutMapping("/{id}/keluarga-mahasiswa/{keluargaId}")
    public ResponseEntity<ApiResDto<KeluargaMahasiswaResDto>> update(
            @PathVariable UUID id,
            @PathVariable UUID keluargaId,
            @Valid @RequestBody KeluargaMahasiswaReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            keluargaService.update(id, keluargaId, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KeluargaMahasiswaResDto>builder()
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

    // Detail Mahasiswa

    @Operation(summary = "Get All Status Semester")
    @GetMapping("/status-semester/{mahasiswaId}")
    public ResponseEntity<ApiResDto<List<StatusSemesterDto>>> getAllStatusSemester  (
            @PathVariable("mahasiswaId") UUID mahasiswaId
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

    @Operation(summary = "Get kemajuan belajar mahasiswa")
    @GetMapping("/{mahasiswaId}/kemajuan-belajar")
    public ResponseEntity<ApiResDto<MahasiswaChartDto>> getKemajuanBelajarMahasiswa(
            @PathVariable UUID mahasiswaId
    ) {
        MahasiswaChartDto dashboardData = service.getDashboardAkademik(mahasiswaId);

        return ResponseEntity.ok(
                ApiResDto.<MahasiswaChartDto>builder()
                        .status("SUCCESS")
                        .message("Data dashboard akademik berhasil diambil.")
                        .data(dashboardData)
                        .build()
        );
    }

    @Operation(summary = "Get Khs")
    @GetMapping("/khs/{mahasiswaId}")
    public ResponseEntity<ApiResDto<HasilStudiDto>> getKhs(@PathVariable("mahasiswaId") UUID mahasiswaId,
                                                                @RequestParam UUID periodeAkademikId) {
        try {
            HasilStudiDto hasilStudi = hasilStudiService.getHasilStudi(mahasiswaId, periodeAkademikId);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<HasilStudiDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(hasilStudi)
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

    @Operation(summary = "Get All Finalisasi MK")
    @GetMapping("/finalisasi-mk/{mahasiswaId}")
    public ResponseEntity<ApiResDto<List<FinalisasiMkDto>>> finalisasiMk    (
            @PathVariable("mahasiswaId") UUID mahasiswaId
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


    @Operation(summary = "Get Mahasiswa Komposisi Nilai")
    @GetMapping("{mahasiswaId}/komposisi-nilai")
    public ResponseEntity<ApiResDto<List<KomposisiNilaiMataKuliahMhsResDto>>> getKomposisiNilaiMahasiswa(
            @PathVariable UUID mahasiswaId,
            @RequestParam String namaPeriode
    ) {
        try {
            List<KomposisiNilaiMataKuliahMhsResDto> nilai = komposisiNilaiMataKuliahMhsService.getKomposisiMataKuliah(mahasiswaId, namaPeriode);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<KomposisiNilaiMataKuliahMhsResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(nilai)
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

    @Operation(summary = "Get mengulang by Periode")
    @GetMapping("/mengulang/{mahasiswaId}")
    public ResponseEntity<ApiResDto<List<MengulangResDto>>> krsMengulang(
            @PathVariable UUID mahasiswaId,
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

    @Operation(summary = "Delete Krs")
    @DeleteMapping("/krs/{mahasiswaId}")
    public ResponseEntity<ApiResDto<Objects>> deletedKrs(@PathVariable("mahasiswaId") UUID id, HttpServletRequest request) {
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

    @Operation(summary = "Get tagihan komponen mahasiswa")
    @GetMapping("/tagihan-komponen/{mahasiswaId}")
    public ResponseEntity<ApiResDto<TagihanKomponenMahasiswaDto>> getTagihanMahasiwa(@PathVariable("mahasiswaId") UUID id) {
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
