package com.siakad.controller.Dosen;

import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.PembimbingAkademikResDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Kelas Kuliah Dosen")
@RestController
@RequestMapping("/dosen/pembimbing-akademik")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOSEN')")
public class DosenPembimbingAkademikController {

}
