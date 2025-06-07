package com.siakad.controller.akademik;

import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.ProgramStudiResDto;
import com.siakad.dto.response.RuanganResDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.RuanganService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Ruangan")
@RestController
@RequestMapping("/akademik/ruangan")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('AKADEMIK_UNIV', 'AKADEMIK_FAK', 'AKADEMIK_PRODI')")
public class RuanganController {

    private final RuanganService service;

    @Operation(summary = "Get All Ruangan")
    @GetMapping
    public ResponseEntity<ApiResDto<List<RuanganResDto>>> getAllProgramStudi() {
        try {
            List<RuanganResDto> all = service.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<RuanganResDto>>builder()
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
}
