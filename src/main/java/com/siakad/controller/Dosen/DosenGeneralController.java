package com.siakad.controller.dosen;

import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.ProgramStudiResDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.ProgramStudiService;
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

@Tag(name = "Program Studi")
@RestController
@RequestMapping("/dosen/program-studi")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('DOSEN')")
public class DosenGeneralController {

    private final ProgramStudiService service;

    @Operation(summary = "Get All Program Studi")
    @GetMapping("")
    public ResponseEntity<ApiResDto<List<ProgramStudiResDto>>> getAllProgramStudi() {
        try {
            List<ProgramStudiResDto> all = service.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResDto.<List<ProgramStudiResDto>>builder()
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