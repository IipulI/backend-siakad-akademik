package com.siakad.controller.mahasiswa;

import com.siakad.dto.response.*;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.HasilStudiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Hasil Studi")
@RestController
@RequestMapping("/mahasiswa")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MAHASISWA')")
public class HasilStudiController {

    private final HasilStudiService hasilStudiService;

    @Operation(summary = "Get KHS by Periode Akademik ID")
    @GetMapping("/khs")
    public ResponseEntity<ApiResDto<HasilStudiDto>> getHasilStudi(
            @RequestParam String namaPeriode
    ) {
        try {
            HasilStudiDto hasilStudi = hasilStudiService.getHasilStudi(namaPeriode);
            return ResponseEntity.status(HttpStatus.OK).body(
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
    @GetMapping("/transkip")
    public ResponseEntity<ApiResDto<TranskipDto>> getTranskip() {
        try {
            List<KrsRincianMahasiswa> rincianMahasiswa = hasilStudiService.getRincianMahasiswa();

            TranskipDto transkipDto = hasilStudiService.buildTranskip(rincianMahasiswa);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<TranskipDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(transkipDto)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
