package com.siakad.controller.dosen;

import com.siakad.dto.request.GetJadwalReqDto;
import com.siakad.dto.request.GetJadwalResDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.JadwalMingguanResDto;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.JadwalDosenService;
import com.siakad.service.JadwalKuliahService;
import com.siakad.service.UserActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Jadwal Dosen")
@RestController
@RequestMapping("/dosen/jadwal")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOSEN')")
public class DosenJadwalController {

    private final JadwalDosenService jadwalDosenService;
    private final UserActivityService userActivityService;
    private final JadwalKuliahService jadwalKuliahService;

    @Operation(summary = "get jadwal semester dosen")
    @GetMapping("/{periodeAkademikId}")
    public ResponseEntity<ApiResDto<?>> jadwalDosen(
            @PathVariable UUID periodeAkademikId,
            @RequestParam(required = false) String hari
    ) {
        try {
            User user = userActivityService.getCurrentUser();
            var dosenId = user.getSiakDosen().getId();

            if (hari != null && !hari.trim().isEmpty()) {
                List<JadwalMingguanResDto> jadwalHarian =
                        jadwalKuliahService.getJadwalHarianDosen(dosenId, periodeAkademikId, hari);

                ApiResDto<List<JadwalMingguanResDto>> response =
                        ApiResDto.<List<JadwalMingguanResDto>>builder()
                                .status(MessageKey.SUCCESS.getMessage())
                                .message(MessageKey.READ.getMessage())
                                .data(jadwalHarian)
                                .build();

                return ResponseEntity.ok(response);
            }
            else {
                Map<String, List<JadwalMingguanResDto>> jadwalMingguan =
                        jadwalKuliahService.getJadwalMingguanDosen(dosenId, periodeAkademikId);

                ApiResDto<Map<String, List<JadwalMingguanResDto>>> response =
                        ApiResDto.<Map<String, List<JadwalMingguanResDto>>>builder()
                                .status(MessageKey.SUCCESS.getMessage())
                                .message(MessageKey.READ.getMessage())
                                .data(jadwalMingguan)
                                .build();

                return ResponseEntity.ok(response);
            }
        }
        catch (ApplicationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
