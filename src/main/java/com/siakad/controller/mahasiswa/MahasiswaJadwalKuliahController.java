package com.siakad.controller.mahasiswa;

import com.siakad.dto.request.KrsReqDto;
import com.siakad.dto.request.ProfilLulusanReqDto;
import com.siakad.dto.response.*;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.JadwalKuliahService;
import com.siakad.service.KrsService;
import com.siakad.service.ProfilLulusanService;
import com.siakad.service.UserActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.*;

@Tag(name = "Jadwal Mahasiswa")
@RestController
@RequestMapping("/mahasiswa/jadwal")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MAHASISWA')")
public class MahasiswaJadwalKuliahController {

    private final KrsService service;
    private final UserActivityService userActivityService;
    private final JadwalKuliahService jadwalKuliahService;

    @Operation(summary = "get jadwal semester")
    @GetMapping("")
    public ResponseEntity<ApiResDto<?>> jadwalMingguan(
            @RequestParam String namaPeriode,
            @RequestParam(required = false) String hari
    ) {
        try {
            User user = userActivityService.getCurrentUser();
            var mahasiswaId = user.getSiakMahasiswa().getId();

            if (hari != null && !hari.trim().isEmpty()) {
                // Call the daily schedule service method
                List<JadwalMingguanResDto> jadwalHarian =
                        jadwalKuliahService.getJadwalHarianMahasiswa(mahasiswaId, namaPeriode, hari);

                ApiResDto<List<JadwalMingguanResDto>> response =
                        ApiResDto.<List<JadwalMingguanResDto>>builder()
                                .status(MessageKey.SUCCESS.getMessage())
                                .message(MessageKey.READ.getMessage())
                                .data(jadwalHarian)
                                .build();

                return ResponseEntity.ok(response);
            } else {
                // Call the weekly schedule service method
                Map<String, List<JadwalMingguanResDto>> jadwalMingguan =
                        jadwalKuliahService.getJadwalMingguanMahasiswa(mahasiswaId, namaPeriode);

                ApiResDto<Map<String, List<JadwalMingguanResDto>>> response =
                        ApiResDto.<Map<String, List<JadwalMingguanResDto>>>builder()
                                .status(MessageKey.SUCCESS.getMessage())
                                .message(MessageKey.READ.getMessage())
                                .data(jadwalMingguan)
                                .build();

                return ResponseEntity.ok(response);
            }
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
