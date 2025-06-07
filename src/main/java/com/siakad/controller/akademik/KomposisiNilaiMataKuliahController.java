package com.siakad.controller.akademik;

import com.siakad.dto.request.KomposisiNilaiMataKuliahReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.KomposisiNilaiMataKuliahResDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.KomposisiPenilaianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "Komposisi Nilai Mata Kuliah")
@RestController
@RequestMapping("/akademik/komposisi-nilai-mata-kuliah")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('KEUANGAN_UNIV', 'KEUANGAN_FAK', 'KEUANGAN_PRODI')")
public class KomposisiNilaiMataKuliahController {

    private final KomposisiPenilaianService service;

    @Operation(summary = "Get All Komposisi nilai mata kuliah")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<List<KomposisiNilaiMataKuliahResDto>>> getAll(@PathVariable UUID id) {
        try {
            List<KomposisiNilaiMataKuliahResDto> allKomposisiNilaiMataKuliah = service.getAllKomposisiNilaiMataKuliah(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<KomposisiNilaiMataKuliahResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(allKomposisiNilaiMataKuliah)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Add Komposisi nilai mata kuliah")
    @PostMapping
    public ResponseEntity<ApiResDto<KomposisiNilaiMataKuliahResDto>> save(
            @Valid @RequestBody KomposisiNilaiMataKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            KomposisiNilaiMataKuliahResDto savedEntity = service.saveKomposisiNilaiMataKuliah(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KomposisiNilaiMataKuliahResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.CREATED.getMessage())
                            .data(savedEntity) // Include the created entity
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error saving komposisi nilai", e);
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
}
