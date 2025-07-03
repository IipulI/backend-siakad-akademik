package com.siakad.controller.mahasiswa;


import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.TagihanMahasiswaDto;
import com.siakad.dto.response.TagihanMhsDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.DashboardService;
import com.siakad.service.UserActivityService;
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
import java.util.UUID;

@Tag(name = "Keuangan Mahasiswa")
@RestController
@RequestMapping("/mahasiswa/keuangan")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MAHASISWA')")
public class MahasiswaKeuanganController {

    private final DashboardService dashboardService;
    private final UserActivityService userActivityService;

    @Operation(summary = "Get tagihan mahasiswa")
    @GetMapping("/histori-tagihan")
    public ResponseEntity<ApiResDto<?>> getHistoriTagihan(
            @RequestParam(required = false) String namaPeriode,
            @RequestParam(required = false) String keyword
    ){
        try {
            UUID mahasiswaId = userActivityService.getCurrentUser().getSiakMahasiswa().getId();

            List<TagihanMhsDto> tagihanMhsDto = dashboardService.getTagihanMhs(mahasiswaId, "lunas", namaPeriode, keyword);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<TagihanMhsDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(tagihanMhsDto)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get tagihan mahasiswa")
    @GetMapping("/tagihan-aktif")
    public ResponseEntity<ApiResDto<?>> getTagihanAktif(){
        try {
            UUID mahasiswaId = userActivityService.getCurrentUser().getSiakMahasiswa().getId();

            List<TagihanMhsDto> tagihanMhsDto = dashboardService.getTagihanMhs(mahasiswaId, "belum lunas", null, null);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<TagihanMhsDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(tagihanMhsDto)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
