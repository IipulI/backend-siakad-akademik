package com.siakad.controller.akademik;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siakad.dto.request.PengumumanReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.PaginationDto;
import com.siakad.dto.response.PengumumanResDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.PengumumanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.UUID;

@Tag(name = "Pengumuman")
@RestController
@RequestMapping("/akademik/pengumuman")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AKADEMIK_UNIV')")
public class PengumumanController {

    private final PengumumanService service;

    private final ObjectMapper objectMapper;

    @Operation(summary = "Add Pengumuman")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResDto<PengumumanResDto>> save(
            @RequestPart(value = "banner", required = false) MultipartFile banner,
            @RequestPart("request") String requestJson,
            HttpServletRequest servletRequest) {

        try {
            PengumumanReqDto request = objectMapper.readValue(requestJson, PengumumanReqDto.class);
            service.save(request, banner, servletRequest);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResDto.<PengumumanResDto>builder()
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

    @Operation(summary = "Get banner")
    @GetMapping("/{id}/banner")
    public ResponseEntity<byte[]> getBanner(@PathVariable UUID id) {
        try {
            byte[] banner = service.getBanner(id);
            if (banner == null || banner.length == 0) {
                throw new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND);
            }
            Tika tika = new Tika();
            String mimeType = tika.detect(banner);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));

            return new ResponseEntity<>(banner, headers, HttpStatus.OK);

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get One Pengumuman")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<PengumumanResDto>> getOne(@PathVariable UUID id) {
        try {
            PengumumanResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<PengumumanResDto>builder()
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

//    @Operation(summary = "Get Pengumuman")
//    @GetMapping
//    public ResponseEntity<ApiResDto<List<PengumumanResDto>>> search(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) String status,
//            @RequestParam(defaultValue = "1") @Min(1) int page,
//            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
//            @RequestParam(defaultValue = "createdAt,desc") String sort
//    ) {
//        try {
//            String[] sortParams = sort.split(",");
//            Sort.Direction direction = sortParams.length > 1 ?
//                    Sort.Direction.fromString(sortParams[1]) : Sort.Direction.DESC;
//            Sort sortObj = Sort.by(direction, sortParams[0]);
//
//            Pageable pageable = PageRequest.of(page - 1, size, sortObj); // page dikurangi 1 karena UI biasanya mulai dari 1
//
//
//            Page<PengumumanResDto> all = service.search(keyword, status, pageable);
//            return ResponseEntity.ok(
//                    ApiResDto.<List<PengumumanResDto>>builder()
//                            .status(MessageKey.SUCCESS.getMessage())
//                            .message(MessageKey.READ.getMessage())
//                            .data(all.getContent())
//                            .pagination(PaginationDto.fromPage(all))
//                            .build()
//            );
//        } catch (ApplicationException e){
//            throw e;
//        } catch (Exception e){
//            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }

    @Operation(summary = "Update Pengumuman")
    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResDto<PengumumanResDto>> update(
            @PathVariable UUID id,
            @RequestPart(value = "banner", required = false) MultipartFile banner,
            @RequestPart("request") String requestJson,
            HttpServletRequest servletRequest
    ) {
        try {
            PengumumanReqDto request = objectMapper.readValue(requestJson, PengumumanReqDto.class);
            service.update(id, request, banner, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<PengumumanResDto>builder()
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

    @Operation(summary = "Delete Pengumuman")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResDto<PengumumanResDto>> delete(@PathVariable UUID id, HttpServletRequest servletRequest) {
        try {
            service.delete(id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<PengumumanResDto>builder()
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
