package com.siakad.controller.akademik;

import com.siakad.dto.request.KrsReqDto;
import com.siakad.dto.request.PenilaianKelasKuliahReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.KrsResDto;
import com.siakad.dto.response.PaginationDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.KrsService;
import com.siakad.service.PenilaianKelasService;
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

@Tag(name = "Penilaian")
@RestController
@RequestMapping("/akademik/penilaian")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AKADEMIK_UNIV')")
public class PenilaianKelasKuliahController {

    private final PenilaianKelasService service;

    @Operation(summary = "Add Penilaian")
    @PutMapping
    public ResponseEntity<ApiResDto<KrsResDto>> save(
            @Valid @RequestBody PenilaianKelasKuliahReqDto request,
            HttpServletRequest servletRequest
    ) {
        try {
            service.updateNilaiKelas(request, servletRequest);
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

//    @Operation(summary = "Get Krs by Pagination")
//    @GetMapping()
//    public ResponseEntity<ApiResDto<List<KrsResDto>>> getPaginated(
//            @RequestParam(required = false) String kelas,
//            @RequestParam(defaultValue = "1") @Min(1) int page,
//            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
//            @RequestParam(defaultValue = "createdAt,desc") String sort) {
//
//        try {
//            // Parse sort parameter
//            String[] sortParams = sort.split(",");
//            Sort.Direction direction = sortParams.length > 1 ?
//                    Sort.Direction.fromString(sortParams[1]) : Sort.Direction.DESC;
//            Sort sortObj = Sort.by(direction, sortParams[0]);
//
//            Pageable pageable = PageRequest.of(page - 1, size, sortObj); // page dikurangi 1 karena UI biasanya mulai dari 1
//
//            Page<KrsResDto> search = service.getPaginated(kelas, pageable);
//
//            return ResponseEntity.ok(
//                    ApiResDto.<List<KrsResDto>>builder()
//                            .status(MessageKey.SUCCESS.getMessage())
//                            .message(MessageKey.READ.getMessage())
//                            .data(search.getContent())
//                            .pagination(PaginationDto.fromPage(search))
//                            .build()
//            );
//        } catch (ApplicationException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
}
