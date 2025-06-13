package com.siakad.controller.Dosen;

import com.siakad.dto.response.*;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.UUID;

@Tag(name = "Kelas Kuliah Dosen")
@RestController
@RequestMapping("/dosen/kelas-kuliah")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOSEN')")
public class DosenKelasKuliahController {

    private final KelasKuliahService service;
    private final UserActivityService userActivityService;
    private final JadwalDosenService jadwalDosenService;
    private final KrsService krsService;

    @Operation(summary = "GET Kelas Kuliah Dosen")
    @GetMapping
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

            User user = userActivityService.getCurrentUser();

            Pageable pageable = PageRequest.of(page - 1, size, sortObj); // page dikurangi 1 karena UI biasanya mulai dari 1
            var dosen = user.getSiakDosen().getNama();

            Page<KelasKuliahResDto> search = service.search(keyword, periodeAkademik, tahunKuriKulum, programStudi, sistemKuliah, dosen, pageable);

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

    @Operation(summary = "Get One Kelas Kuliah for detail kelas kuliah")
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

    @Operation(summary = "Get Jadwal Kuliah Dosen by Kelas Kuliah Id")
    @GetMapping("/{id}/jadwal-kelas")
    public ResponseEntity<ApiResDto<List<JadwalDto>>> getJadwal(@PathVariable UUID id) {
        try {

            User user = userActivityService.getCurrentUser();
            var dosenId = user.getSiakDosen().getId();

            System.out.println(dosenId);

            List<JadwalDto> all = jadwalDosenService.getByDosenId(id, dosenId);
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
}
