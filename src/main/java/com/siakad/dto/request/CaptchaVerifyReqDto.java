package com.siakad.dto.request;

public class CaptchaVerifyReqDto {
    private int userPuzzlePieceX; // The X coordinate submitted by the user

    // Constructor
    public CaptchaVerifyReqDto() {
    }

    public CaptchaVerifyReqDto(int userPuzzlePieceX) {
        this.userPuzzlePieceX = userPuzzlePieceX;
    }

    // Getter
    public int getUserPuzzlePieceX() {
        return userPuzzlePieceX;
    }

    // Setter
    public void setUserPuzzlePieceX(int userPuzzlePieceX) {
        this.userPuzzlePieceX = userPuzzlePieceX;
    }
}