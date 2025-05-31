package com.siakad.controller.mahasiswa;

import com.siakad.dto.request.KrsReqDto;
import com.siakad.dto.request.ProfilLulusanReqDto;
import com.siakad.dto.response.*;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.KrsService;
import com.siakad.service.ProfilLulusanService;
import com.siakad.service.UserActivityService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "KRS")
@RestController
@RequestMapping("/akademik/krs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MAHASISWA')")
public class KrsController {

    private final KrsService service;
    private final UserActivityService userActivityService;

    @Operation(summary = "Add Krs")
    @PostMapping
    public ResponseEntity<ApiResDto<KrsResDto>> save(
            @Valid @RequestBody KrsReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.save(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KrsResDto>builder()
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

    @Operation(summary = "Update Krs")
    @PutMapping
    public ResponseEntity<ApiResDto<KrsResDto>> update(
            @Valid @RequestBody KrsReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.update(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KrsResDto>builder()
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

    @Operation(summary = "Get Krs by Pagination")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<KrsResDto>>> getPaginated(
            @RequestParam(required = false) String kelas,
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

            Page<KrsResDto> search = service.getPaginated(kelas, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<KrsResDto>>builder()
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

    @Operation(summary = "Update Statu from draft to menunggu")
    @PutMapping("/status")
    public ResponseEntity<ApiResDto<KrsResDto>> updateStatusFromDraftToMenunggu(
            HttpServletRequest servletRequest
    ) {
        try {
            service.updateStatus(servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KrsResDto>builder()
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

    @Operation(summary = "get mengulang")
    @GetMapping("/mengulang")
    public ResponseEntity<ApiResDto<List<MengulangResDto>>> krsMengulang() {
        try {
            User user = userActivityService.getCurrentUser();
            var mahasiswa = user.getSiakMahasiswa().getId();

            List<MengulangResDto> data = service.getAllMengulang(mahasiswa);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<MengulangResDto>>builder()
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
}
