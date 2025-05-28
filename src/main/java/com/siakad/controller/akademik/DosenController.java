package com.siakad.controller.akademik;

import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.DosenResDto;
import com.siakad.dto.response.ProgramStudiResDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.DosenService;
import com.siakad.service.ProgramStudiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Dosen")
@RestController
@RequestMapping("/akademik/dosen")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AKADEMIK_UNIV')")
public class DosenController {

    private final DosenService service;

    @Operation(summary = "Get All Dosen")
    @GetMapping
    public ResponseEntity<ApiResDto<List<DosenResDto>>> getAllDosen(
            @RequestParam(required = false) String keyword
    ) {
        try {
            List<DosenResDto> all = service.getAll(keyword);
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
}
