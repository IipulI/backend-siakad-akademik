package com.siakad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaResDto {
    private String holedImageBase64;
    private String puzzlePieceBase64;
    private int puzzlePieceWidth;
    private int puzzlePieceHeight;
    private int initialPuzzlePieceX;
    private int puzzlePieceY;
    private int imageWidth;
    private int imageHeight;
    private int correctPuzzlePieceX;
}