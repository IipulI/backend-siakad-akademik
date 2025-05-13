package com.siakad.controller.akademik;

import com.siakad.dto.request.CapaianMataKuliahReqDto;
import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.CapaianMataKuliahService;
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

@Tag(name = "Capaian Mata Kuliah")
@RestController
@RequestMapping("/akademik/mata-kuliah")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AKADEMIK_UNIV')")
public class CapaianMataKuliahController {

    private final CapaianMataKuliahService service;

    @Operation(summary = "Add Capaian Mata Kuliah")
    @PostMapping("/{idMataKuliah}/capaian-mata-kuliah")
    public ResponseEntity<ApiResDto<CapaianMataKuliahResDto>> save(
            @PathVariable UUID idMataKuliah,
            @Valid @RequestBody CapaianMataKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.save(idMataKuliah, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<CapaianMataKuliahResDto>builder()
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

    @Operation(summary = "Get One Capaian Mata Kulaih")
    @GetMapping("/{idMataKuliah}/capaian-mata-kuliah/{id}")
    public ResponseEntity<ApiResDto<CapaianMataKuliahResDto>> getOne(
            @PathVariable UUID idMataKuliah,
            @PathVariable UUID id) {
        try {
            CapaianMataKuliahResDto one = service.getOne(idMataKuliah, id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<CapaianMataKuliahResDto>builder()
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

    @Operation(summary = "Get Capaian Mata Kuliah By Pagiantion")
    @GetMapping("/{idMataKuliah}/capaian-mata-kuliah")
    public ResponseEntity<ApiResDto<List<CapaianMataKuliahResDto>>> getPaginated(
            @PathVariable UUID idMataKuliah,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        try {
            String[] sortParams = sort.split(",");
            Sort.Direction direction = sortParams.length > 1 ?
                    Sort.Direction.fromString(sortParams[1]) : Sort.Direction.DESC;
            Sort sortObj = Sort.by(direction, sortParams[0]);

            Pageable pageable = PageRequest.of(page - 1, size, sortObj); // page dikurangi 1 karena UI biasanya mulai dari 1

            Page<CapaianMataKuliahResDto> search = service.getPaginate(idMataKuliah, pageable);

            return ResponseEntity.ok(
                    ApiResDto.<List<CapaianMataKuliahResDto>>builder()
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

    @Operation(summary = "Update Capaian Mata Kuliah")
    @PutMapping("/{idMataKuliah}/capaian-mata-kuliah/{id}")
    public ResponseEntity<ApiResDto<CapaianMataKuliahResDto>> update(
            @PathVariable UUID idMataKuliah,
            @PathVariable UUID id,
            @Valid @RequestBody CapaianMataKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.update(idMataKuliah, id, request, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResDto.<CapaianMataKuliahResDto>builder()
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

    @Operation(summary = "Delete Capaian Mata Kuliah")
    @DeleteMapping("/{idMataKuliah}/capaian-mata-kuliah/{id}")
    public ResponseEntity<ApiResDto<CapaianPembelajaranLulusanResDto>> delete(
            @PathVariable UUID idMataKuliah,
            @PathVariable UUID id,
            HttpServletRequest servletRequest) {
        try {
            service.delete(idMataKuliah,id, servletRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<CapaianPembelajaranLulusanResDto>builder()
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

    @Operation(summary = "Get all mata kuliah for add capaian")
    @GetMapping("/all")
    public ResponseEntity<ApiResDto<List<MataKuliahCpmkMappingDto>>> getAll(
            @RequestParam(required = false) String tahunKurikulum
    ) {
        try {
            List<MataKuliahCpmkMappingDto> one = service.getMataKuliahWithCpmkStatus(tahunKurikulum);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<MataKuliahCpmkMappingDto>>builder()
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

}
