package com.siakad.controller.akademik;

import com.siakad.dto.request.JadwalKuliahReqDto;
import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.JadwalKuliahService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Jadwal Kuliah")
@RestController
@RequestMapping("/akademik/jadwal-kuliah")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AKADEMIK_UNIV')")
public class JadwalKelasController {


    private final JadwalKuliahService service;

    @Operation(summary = "Add Jadwal Kelas")
    @PostMapping
    public ResponseEntity<ApiResDto<JadwalKuliahResDto>> save(
            @Valid @RequestBody JadwalKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.create(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<JadwalKuliahResDto>builder()
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

    @Operation(summary = "Get All Jadwal Kuliah")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<JadwalKuliahResDto>>> getAll(){
        try {
            List<JadwalKuliahResDto> all = service.getAll();
            return ResponseEntity.ok(
                    ApiResDto.<List<JadwalKuliahResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(all)
                            .build());

        }catch (ApplicationException e) {
            throw e;
        }catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Dosen Pengajar by Kelas ID")
    @GetMapping("/dosen-pengajar/{kelasId}")
    public ResponseEntity<ApiResDto<DetailKelasDosenPengajarResDto>> getDosenPengajarByKelasId(
            @PathVariable UUID kelasId) {
        try {
            DetailKelasDosenPengajarResDto result = service.getDetailKelasDosenPengajar(kelasId);
            return ResponseEntity.ok(
                    ApiResDto.<DetailKelasDosenPengajarResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message("Berhasil mengambil data dosen pengajar untuk kelas")
                            .data(result)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @Operation(summary = "Delete Jadwal Kuliah")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<JadwalKuliahResDto>> delete(@PathVariable UUID id,
                                                                HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<JadwalKuliahResDto>builder()
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
