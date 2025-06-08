package com.siakad.controller;

import com.siakad.dto.response.CaptchaResDto;
import com.siakad.dto.request.CaptchaVerifyReqDto;
import com.siakad.service.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Captcha")
@RestController
@RequestMapping("/captcha")
@CrossOrigin(origins = "http://localhost:5173") // Or whatever your React dev server's URL is
public class CaptchaController {

    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    /**
     * Endpoint to generate a new CAPTCHA puzzle.
     * Stores the correct solution in the user's session.
     *
     * @param session The current HTTP session.
     * @return ResponseEntity containing the CaptchaResponse DTO or an error.
     */
    @Operation(summary = "Generate Captcha")
    @GetMapping("/generate")
    public ResponseEntity<CaptchaResDto> generateCaptcha(HttpSession session) {
        try {
            CaptchaResDto response = captchaService.generatePuzzle(session);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            System.err.println("Error generating CAPTCHA: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return null body or a specific error DTO
        }
    }

    /**
     * Endpoint to verify the user's CAPTCHA solution.
     *
     * @param request The CaptchaVerificationRequest containing the user's puzzle piece X coordinate.
     * @param session The current HTTP session.
     * @return ResponseEntity indicating success or failure.
     */
    @Operation(summary = "Verify Captcha")
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyCaptcha(@RequestBody CaptchaVerifyReqDto request, HttpSession session) {
        boolean isCorrect = captchaService.verifyPuzzle(request.getUserPuzzlePieceX(), session);
        return ResponseEntity.ok(isCorrect);
    }
}