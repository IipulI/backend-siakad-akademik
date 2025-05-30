package com.siakad.controller.akademik;


import com.siakad.dto.response.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@Tag(name = "Akademik")
@RestController
@RequestMapping("/akademik")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AKADEMIK_UNIV')")
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
