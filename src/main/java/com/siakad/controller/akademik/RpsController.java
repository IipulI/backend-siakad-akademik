package com.siakad.controller.akademik;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siakad.dto.request.KelasRpsReqDto;
import com.siakad.dto.request.RpsReqDto;
import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.RpsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

@Tag(name = "RPS")
@RestController
@RequestMapping("/akademik/rps")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AKADEMIK_UNIV')")
public class RpsController {

    private final RpsService service;

    private final ObjectMapper objectMapper;

    @Operation(summary = "Add Rps")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResDto<Objects>> save(
            @RequestPart(value = "dokumenRps", required = false) MultipartFile dokumenRps,
            @RequestPart("request") String requestJson,
            HttpServletRequest servletRequest) {

        try {
            RpsReqDto request = objectMapper.readValue(requestJson, RpsReqDto.class);
            service.create(request, dokumenRps, servletRequest);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResDto.<Objects>builder()
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

    @Operation(summary = "Get DokumenRps")
    @GetMapping("/{id}/dokumen-rps")
    public ResponseEntity<byte[]> getDokumenRps(@PathVariable UUID id) {
        try {
            byte[] dokumenRps = service.getDokumenRps(id);
            if (dokumenRps == null || dokumenRps.length == 0) {
                throw new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND);
            }
            Tika tika = new Tika();
            String mimeType = tika.detect(dokumenRps);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));

            return new ResponseEntity<>(dokumenRps, headers, HttpStatus.OK);

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get One Rps")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<RpsResDto>> getOne(@PathVariable UUID id) {
        try {
            RpsResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<RpsResDto>builder()
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

    @Operation(summary = "Get Rps by Pagination")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<RpsResDto>>> getPaginated(
            @RequestParam(required = false) String tahunKurikulum,
            @RequestParam(required = false) String programStudi,
            @RequestParam(required = false) String periodeAkademik,
            @RequestParam(required = false) Boolean hasKelas,
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

            Page<RpsResDto> search = service.getPaginate(tahunKurikulum, programStudi, periodeAkademik, hasKelas, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<RpsResDto>>builder()

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

    @Operation(summary = "Update Rps")
    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResDto<RpsResDto>> update(
            @PathVariable UUID id,
            @RequestPart(value = "dokumenRps", required = false) MultipartFile dokumenRps,
            @RequestPart("request") String requestJson,
            HttpServletRequest servletRequest
    ) {
        try {
            RpsReqDto request = objectMapper.readValue(requestJson, RpsReqDto.class);
            service.update(id, request, dokumenRps, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<RpsResDto>builder()
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

    @Operation(summary = "Delete Rps")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<RpsResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<RpsResDto>builder()
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

    @Operation(summary = "Add Kelas Kuliah")
    @PostMapping("/kelas-rps")
    public ResponseEntity<ApiResDto<KelasRpsResponseDto>> createKelas(
            @Valid @RequestBody KelasRpsReqDto kelasRpsReqDto,
            HttpServletRequest servletRequest
    ) {
        try {
            service.createKelas(kelasRpsReqDto, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<KelasRpsResponseDto>builder()
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

}
