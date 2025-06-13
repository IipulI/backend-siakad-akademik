package com.siakad.controller.akademik;


import com.siakad.dto.response.*;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Tag(name = "Akademik")
@RestController
@RequestMapping("/akademik")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('AKADEMIK_UNIV', 'AKADEMIK_FAK', 'AKADEMIK_PRODI')")
public class AkademikController {

    private final KelasKuliahService service;

    @Operation(summary = "Ganti semester")
    @PutMapping("/ganti-semester")
    public ResponseEntity<ApiResDto<Objects>> gantiSemester(){
        try {

            service.gantiSemester();

            return ResponseEntity.ok(
                    ApiResDto.<Objects>builder()
                            .status(MessageKey.SUCCESS.getMessage())
                            .message(MessageKey.READ.getMessage())
                            .build()
            );
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ExceptionType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
