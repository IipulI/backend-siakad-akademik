package com.siakad.controller.akademik;

import com.siakad.dto.request.KelasKuliahReqDto;
import com.siakad.dto.request.PembimbingAkademikReqDto;
import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.DosenService;
import com.siakad.service.PembimbingAkademikService;
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

@Tag(name = "Pembimbing Akademik")
@RestController
@RequestMapping("/akademik/pembimbing-akademik")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('AKADEMIK_UNIV', 'AKADEMIK_FAK', 'AKADEMIK_PRODI')")
public class PembimbingAkademikController {

    private final PembimbingAkademikService service;
    private final DosenService dosenService;

    @Operation(summary = "Add Bimbingan Akademik")
    @PostMapping
    public ResponseEntity<ApiResDto<PembimbingAkademikResDto>> save(
            @Valid @RequestBody PembimbingAkademikReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.save(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<PembimbingAkademikResDto>builder()
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

    @Operation(summary = "Get All Dosen")
    @GetMapping("/dosen")
    public ResponseEntity<ApiResDto<List<DosenResDto>>> getAllDosen(
            @RequestParam(required = false) String keyword
    ) {
        try {
            List<DosenResDto> all = dosenService.getAll(keyword);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<DosenResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(all)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Kelas Kuliah By Pagination")
    @GetMapping("/all")
    public ResponseEntity<ApiResDto<List<PembimbingAkademikResDto>>> getPaginated(
            @RequestParam(required = false) String periodeAkademik,
            @RequestParam(required = false) String programStudi,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String angkatan,
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

            Page<PembimbingAkademikResDto> search = service.getAllByPaginate(periodeAkademik, programStudi, semester, angkatan, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<PembimbingAkademikResDto>>builder()
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
}
