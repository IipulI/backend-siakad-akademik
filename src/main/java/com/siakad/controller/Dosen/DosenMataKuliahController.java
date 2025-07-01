package com.siakad.controller.dosen;

import com.siakad.dto.response.*;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.MataKuliahService;
import com.siakad.service.RpsService;
import com.siakad.service.UserActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Mata Kuliah Dosen")
@RestController
@RequestMapping("/dosen/mata-kuliah")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOSEN')")
public class DosenMataKuliahController {

    private final MataKuliahService service;
    private final UserActivityService userActivityService;
    private final MataKuliahService mataKuliahService;
    private final RpsService rpsService;

    @Operation(summary = "Get list mata kuliah")
    @GetMapping()
    public ResponseEntity<ApiResDto<List<MataKuliahResDto>>> getMataKuliah(
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

            // Tahun Kurikulum
            User user = userActivityService.getCurrentUser();

            Page<MataKuliahResDto> search = service.getPaginated(keyword, user.getSiakDosen().getId(), pageable);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<MataKuliahResDto>>builder()
                            .message(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(search.getContent())
                            .pagination(PaginationDto.fromPage(search))
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

    @Operation(summary = "Get One Mata Kuliah")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResDto<MataKuliahResDto>> getOne(@PathVariable UUID id) {
        try {
            MataKuliahResDto one = service.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<MataKuliahResDto>builder()
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

    @Operation(summary = "Get RPS mata kuliah")
    @GetMapping("/{id}/rps")
    public ResponseEntity<ApiResDto<RpsDetailResDto>> getMataKuliahRpsDetail(
            @PathVariable UUID id
    ) {
        try {
            RpsDetailResDto detail = rpsService.getOneRpsDetail(id, "mataKuliah");

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<RpsDetailResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(detail)
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

    @Operation(summary = "Get MataKuliahCplCpmk")
    @GetMapping("{id}/cpl-cpmk/")
    public ResponseEntity<ApiResDto<MataKuliahCplCpmkResDto>> getCplCpmkMataKuliah  (
            @PathVariable("id") UUID id
    ) {
        try {

            MataKuliahCplCpmkResDto mataKuliahCplCpmk = mataKuliahService.getMataKuliahCplCpmk(id);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<MataKuliahCplCpmkResDto>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .data(mataKuliahCplCpmk)
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
