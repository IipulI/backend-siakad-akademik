package com.siakad.controller.mahasiswa;

import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.PaginationDto;
import com.siakad.dto.response.PengumumanResDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.PengumumanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "Pengumuman Mahasiswa")
@RestController
@RequestMapping("/mahasiswa/pengumuman")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MAHASISWA')")
public class MahasiswaPengumumanController {

    private final PengumumanService pengumumanService;

    @Operation(summary = "Get Pengumuman")
    @GetMapping
    public ResponseEntity<ApiResDto<List<PengumumanResDto>>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        try {
            String[] sortParams = sort.split(",");
            Sort.Direction direction = sortParams.length > 1 ?
                    Sort.Direction.fromString(sortParams[1]) : Sort.Direction.DESC;
            Sort sortObj = Sort.by(direction, sortParams[0]);

            Pageable pageable = PageRequest.of(page - 1, size, sortObj); // page dikurangi 1 karena UI biasanya mulai dari 1


            Page<PengumumanResDto> all = pengumumanService.search(keyword, null, pageable);
            return ResponseEntity.ok(
                    ApiResDto.<List<PengumumanResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(all.getContent())
                            .pagination(PaginationDto.fromPage(all))
                            .build()
            );
        } catch (ApplicationException e){
            throw e;
        } catch (Exception e){
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get One Pengumuman")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<PengumumanResDto>> getOne(@PathVariable UUID id) {
        try {
            PengumumanResDto one = pengumumanService.getOne(id);
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

    @Operation(summary = "Get banner")
    @GetMapping("/{id}/banner")
    public ResponseEntity<byte[]> getBanner(@PathVariable UUID id) {
        try {
            byte[] banner = pengumumanService.getBanner(id);
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
}
