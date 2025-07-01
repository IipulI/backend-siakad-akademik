package com.siakad.controller.mahasiswa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siakad.dto.request.KeluargaMahasiswaReqDto;
import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.KeluargaMahasiswaService;
import com.siakad.service.MahasiswaService;
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
import java.util.UUID;

@Tag(name = "Profil Mahasiswa")
@RestController
@RequestMapping("/mahasiswa/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MAHASISWA')")
public class ProfileController {

    private final MahasiswaService service;

    private final KeluargaMahasiswaService keluargaService;

    private final ObjectMapper objectMapper;

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

//    @Operation(summary = "Get Mahasiswa By Pagiantion")
//    @GetMapping()
//    public ResponseEntity<ApiResDto<List<MahasiswaResDto>>> getPaginated(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        try {
//            Page<MahasiswaResDto> paginated = service.getPaginated(page, size);
//
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    ApiResDto.<List<MahasiswaResDto>>builder()
//                            .status(MessageKey.SUCCESS.getMessage())
//                            .message(MessageKey.READ.getMessage())
//                            .data(paginated.getContent())
//                            .pagination(PaginationDto.fromPage(paginated))
//                            .build()
//            );
//        } catch (ApplicationException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }

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
    @GetMapping("/{id}/keluaga-mahasiswa/{idKeluarga}")
    public ResponseEntity<ApiResDto<KeluargaMahasiswaResDto>> getOne(
            @PathVariable UUID id,
            @PathVariable UUID idKeluarga) {
        try {
            var data = keluargaService.getOne(id, idKeluarga);
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

    @Operation(summary = "Get Profile Info")
    @GetMapping("/info")
    public ResponseEntity<ApiResDto<ProfileInfo>> getProfileInfo() {
        try {
            var data = service.getProfileInfo();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<ProfileInfo>builder()
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
    @PutMapping("/{id}/keluarga-mahasiswa/{idKeluarga}")
    public ResponseEntity<ApiResDto<KeluargaMahasiswaResDto>> update(
            @PathVariable UUID id,
            @PathVariable UUID idKeluarga,
            @Valid @RequestBody KeluargaMahasiswaReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            keluargaService.update(id, idKeluarga, request, servletRequest);
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
}
