package com.siakad.controller.akademik;

import com.siakad.dto.request.*;
import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Tag(name = "Kelas Kuliah")
@RestController
@RequestMapping("/akademik/kelas-kuliah")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('AKADEMIK_UNIV', 'AKADEMIK_FAK', 'AKADEMIK_PRODI')")
public class KelasKuliahController {

    private final KelasKuliahService service;
    private final PenilaianKelasService penilaianKelasService;
    private final JadwalDosenService jadwalDosenService;
    private final KrsService krsService;
    private final RpsService rpsService;

    @Operation(summary = "Add Kelas Kuliah")
    @PostMapping
    public ResponseEntity<ApiResDto<KelasKuliahResDto>> save(
            @Valid @RequestBody KelasKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.create(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KelasKuliahResDto>builder()
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

    @Operation(summary = "Update Kelas Kuliah")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResDto<KelasKuliahResDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody KelasKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.update(request, id, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KelasKuliahResDto>builder()
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

    @Operation(summary = "Get One Kelas Kuliah")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<KelasKuliahResDto>> getOne(@PathVariable UUID id) {
        try {
            KelasKuliahResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<KelasKuliahResDto>builder()
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

    @Operation(summary = "Data Kelas Kuliah untuk mapping di rps")
    @GetMapping("/map-rps")
    public ResponseEntity<ApiResDto<List<KelasKuliahDto>>> getAll() {
        try {
            List<KelasKuliahDto> all = service.getAllKelasKuliah();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<KelasKuliahDto>>builder()
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



    @Operation(summary = "Get Kelas Kuliah By Pagination")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<KelasKuliahResDto>>> getPaginated(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String periodeAkademik,
            @RequestParam(required = false) String tahunKuriKulum,
            @RequestParam(required = false) String programStudi,
            @RequestParam(required = false) String sistemKuliah,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        try {
            // Parse sort parameter
            String[] sortParams = sort.split(",");
            Sort.Direction direction = sortParams.length > 1 ?
                    Sort.Direction.fromString(sortParams[1]) : Sort.Direction.DESC;
            Sort sortObj = Sort.by(direction, sortParams[0]);

            Pageable pageable = PageRequest.of(page - 1, size, sortObj); // page dikurangi 1 karena UI biasanya mulai dari 1

            Page<KelasKuliahResDto> search = service.search(keyword, periodeAkademik, null, tahunKuriKulum, programStudi, sistemKuliah, null, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<KelasKuliahResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(search.getContent())
                            .pagination(PaginationDto.fromPage(search))
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Delete Kelas Kuliah")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<KelasKuliahResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<KelasKuliahResDto>builder()
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

    @Operation(summary = "Assign Dosen to Jadwal Kelas Kuliah")
    @PutMapping(value = "/{id}/jadwal-dosen")
    public ResponseEntity<ApiResDto<JadwalKuliahResDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody JadwalDosenReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            jadwalDosenService.save(id,request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<JadwalKuliahResDto>builder()
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

    @Operation(summary = "Get Jadwal Dosen by kelas kuliah ID")
    @GetMapping("/{id}/jadwal-dosen")
    public ResponseEntity<ApiResDto<List<JadwalDto>>> getAll(@PathVariable UUID id) {
        try {
            List<JadwalDto> all = jadwalDosenService.getAll(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<JadwalDto>>builder()
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

    @Operation(summary = "Get Peserta Kelas by Kelas Kuliah ID")
    @GetMapping("/{id}/peserta-kelas")
    public ResponseEntity<ApiResDto<List<PesertaKelas>>> getPesertaKelas(@PathVariable UUID id) {
        try {
            List<PesertaKelas> all = krsService.getPesertaKelas(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<PesertaKelas>>builder()
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

    @Operation(summary = "Get eligible peserta kuliah")
    @GetMapping("/{id}/eligible-peserta-kelas")
    public ResponseEntity<ApiResDto> getEligiblePesertaKelas(
            @PathVariable UUID id,
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String periodeMasuk,
            @RequestParam(required = false) String sistemKuliah
    ) {
        try {

            List<EligiblePesertaKelasDto> elgiblePesertaKelas = krsService.getEligiblePesertaKelas(id, nama, periodeMasuk, sistemKuliah);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(elgiblePesertaKelas)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Add Peserta Mahasiswa by Kelas Kuliah ID")
    @PostMapping("/{id}/peserta-kelas")
    public ResponseEntity<ApiResDto<Objects>> updatePesertaKelas(
            @PathVariable UUID id,
            @Valid @RequestBody PesertaKelasReqDto request,
            HttpServletRequest serletRequest
            ) {
        try {
            krsService.addPesertaKelas(id, request, serletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<Objects>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATED.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e){
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Delete Peserta Mahasiswa by Kelas Kuliah ID")
    @DeleteMapping("/{id}/peserta-kelas")
    public ResponseEntity<ApiResDto<Objects>> deletePesertaKelas(
            @PathVariable UUID id,
            @Valid @RequestBody PesertaKelasReqDto request,
            HttpServletRequest serletRequest
    ) {
        try {
            krsService.deletePesertaKelas(id, request, serletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<Objects>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.DELETED.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e){
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Pindah Kelas Peserta Mahasiswa by Kelas Kuliah ID")
    @PutMapping("/{id}/peserta-kelas/pindah-kelas")
    public ResponseEntity<ApiResDto<Objects>> pindahKelas(
            @PathVariable UUID id,
            @Valid @RequestBody PindahKelasReqDto request,
            HttpServletRequest serletRequest
    ) {
        try {
            krsService.pindahKelasPeserta(id, request, serletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<Objects>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATED.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e){
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Rps by kelas kuliah ID")
    @GetMapping("/{id}/kelas-rps")
    public ResponseEntity<ApiResDto<RpsResDto>> getOneKelasRps(@PathVariable UUID id) {
        try {

            RpsResDto rpsByKelas = rpsService.getRpsByKelas(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<RpsResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(rpsByKelas)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Add Penilaian")
    @PutMapping("/{id}/penilaian")
    public ResponseEntity<ApiResDto<KrsResDto>> save(
            @PathVariable UUID id,
            @Valid @RequestBody PenilaianKelasKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            penilaianKelasService.updateNilaiKelas(id,request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KrsResDto>builder()
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

    @Operation(summary = "Get All Penilaian By Kelas ID")
    @GetMapping("/{id}/penilaian")
    public ResponseEntity<ApiResDto<PenilaianKelasResDto>> getAllPenilaian(@PathVariable UUID id){
        try {
            PenilaianKelasResDto allPenilaianKelas = penilaianKelasService.getAllPenilaianKelas(id);

            return ResponseEntity.ok(
                    ApiResDto.<PenilaianKelasResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(allPenilaianKelas)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Save jadwal ujian kelas")
    @PostMapping("{id}/jadwal-ujian")
    public ResponseEntity<ApiResDto<JadwalUjianResDto>> save(
            @PathVariable UUID id,
            @Valid @RequestBody JadwalUjianReqDto request,
            HttpServletRequest servletRequest
    ){
        try {
            jadwalDosenService.saveJadwalUjian(id, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<JadwalUjianResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.CREATED.getMessage())
                            .build()
            );
        }
        catch (ApplicationException e) {
            throw e;
        }
        catch (Exception e){
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Hapus jadwal ujian kelas")
    @DeleteMapping("{id}/jadwal-ujian/{jadwalId}")
    public ResponseEntity<ApiResDto<Objects>> delete(
            @PathVariable UUID id,
            @PathVariable UUID jadwalId,
            HttpServletRequest servletRequest
    ){
        try {
            jadwalDosenService.deleteJadwalUjian(id, jadwalId, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<Objects>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.DELETED.getMessage())
                            .build()
            );
        }
        catch (ApplicationException e) {
            throw e;
        }
        catch (Exception e){
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get jadwal ujian kelas")
    @GetMapping("/{id}/jadwal-ujian")
    public ResponseEntity<ApiResDto<List<JadwalUjianResDto>>> getJadwalUjian(@PathVariable UUID id){
        try {
            List<JadwalUjianResDto> result = jadwalDosenService.getAllJadwalUjian(id);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<JadwalUjianResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(result)
                            .build()
            );
        }
        catch (ApplicationException e) {
            throw e;
        }
        catch (Exception e){
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
