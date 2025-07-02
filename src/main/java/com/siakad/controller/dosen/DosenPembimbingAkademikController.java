package com.siakad.controller.dosen;

import com.siakad.dto.request.UpdateStatusKrsReqDto;
import com.siakad.dto.response.*;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.KrsService;
import com.siakad.service.PembimbingAkademikService;
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

@Tag(name = "Kelas Kuliah Dosen")
@RestController
@RequestMapping("/dosen/pembimbing-akademik")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOSEN')")
public class DosenPembimbingAkademikController {

    private final UserActivityService userActivityService;
    private final PembimbingAkademikService service;
    private final KrsService krsService;

    @Operation(summary = "Get Pembimbing Akademik By Pagination")
    @GetMapping("/all")
    public ResponseEntity<ApiResDto<List<PembimbingAkademikResDto>>> getPaginated(
            @RequestParam() String periodeAkademik,
            @RequestParam(required = false) String programStudi,
            @RequestParam(required = false) String angkatan,
            @RequestParam(required = false) String statusKrs,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean hasPembimbing,
            @RequestParam(required = false) String statusMahasiswa,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ){
        try {
            User user = userActivityService.getCurrentUser();
            var dosenId = user.getSiakDosen().getId();

            String[] sortParams = sort.split(",");
            Sort.Direction direction = sortParams.length > 1 ?
                    Sort.Direction.fromString(sortParams[1]) : Sort.Direction.DESC;
            Sort sortObj = Sort.by(direction, sortParams[0]);

            Pageable pageable = PageRequest.of(page - 1, size, sortObj); // page dikurangi 1 karena UI biasanya mulai dari 1

            Page<PembimbingAkademikResDto> data = service.getAllPaginated(
                    programStudi, periodeAkademik, dosenId, keyword,
                    angkatan, statusMahasiswa, statusKrs, hasPembimbing, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<PembimbingAkademikResDto>>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(data.getContent())
                            .pagination(PaginationDto.fromPage(data))
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Setuju Krs Mahasiswa")
    @PostMapping("/setuju")
    public ResponseEntity<ApiResDto<KrsResDto>> setujuKrs(
            @Valid @RequestBody UpdateStatusKrsReqDto request,
            HttpServletRequest servletRequest
    ){
        try {
            krsService.updateStatusKrsSetuju(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KrsResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATED.getMessage())
                            .build()
            );
        }
        catch (ApplicationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Tolak Krs Mahasiswa")
    @PostMapping("/tolak")
    public ResponseEntity<ApiResDto<KrsResDto>> tolakKrs(
            @Valid @RequestBody UpdateStatusKrsReqDto request,
            HttpServletRequest servletRequest
    ){
        try {
            krsService.updateStatusKrsTolak(request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<KrsResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.UPDATED.getMessage())
                            .build()
            );
        }
        catch (ApplicationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Get Detail KRS Mahasiswa")
    @GetMapping("/detail-krs/{mahasiswaId}")
    public ResponseEntity<ApiResDto<KrsMenungguResDto>> getDetailKRSMahasiswa(
            @PathVariable UUID mahasiswaId
    ) {
        try {
            KrsMenungguResDto result = krsService.getDetailKrsMahasiswa(mahasiswaId);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<KrsMenungguResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(result)
                            .build()
            );
        }
        catch (ApplicationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
