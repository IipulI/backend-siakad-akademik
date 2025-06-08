package com.siakad.service;

import com.siakad.dto.response.CaptchaResDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

@Service
public class CaptchaService {

    private static final int PUZZLE_PIECE_WIDTH = 50;
    private static final int PUZZLE_PIECE_HEIGHT = 50;
    private static final int TOLERANCE = 5;

    private final Random random = new Random();
    private final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public CaptchaResDto generatePuzzle(HttpSession session) throws IOException {
        Resource[] imageResources = resourcePatternResolver.getResources("classpath:/static/images/*");
        if (imageResources.length == 0) {
            throw new IOException("No images found in src/main/resources/static/images/");
        }
        Resource selectedImageResource = imageResources[random.nextInt(imageResources.length)];
        BufferedImage originalImage = ImageIO.read(selectedImageResource.getInputStream());

        if (originalImage.getWidth() < PUZZLE_PIECE_WIDTH || originalImage.getHeight() < PUZZLE_PIECE_HEIGHT) {
            throw new IOException("Selected image is too small for puzzle generation.");
        }

        int maxPossibleX = originalImage.getWidth() - PUZZLE_PIECE_WIDTH;
        if (maxPossibleX < 0) maxPossibleX = 0;
        int correctX = random.nextInt(maxPossibleX + 1);

        int puzzleY = random.nextInt(originalImage.getHeight() - PUZZLE_PIECE_HEIGHT + 1);

        session.setAttribute("captchaCorrectX", correctX);
        session.setAttribute("captchaPuzzleY", puzzleY);

        BufferedImage holedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dHoled = holedImage.createGraphics();
        g2dHoled.drawImage(originalImage, 0, 0, null);
        g2dHoled.setComposite(AlphaComposite.Clear);
        g2dHoled.fillRect(correctX, puzzleY, PUZZLE_PIECE_WIDTH, PUZZLE_PIECE_HEIGHT);
        g2dHoled.dispose();

        BufferedImage puzzlePiece = originalImage.getSubimage(correctX, puzzleY, PUZZLE_PIECE_WIDTH, PUZZLE_PIECE_HEIGHT);

        String holedImageBase64 = encodeImageToBase64(holedImage, "png");
        String puzzlePieceBase64 = encodeImageToBase64(puzzlePiece, "png");

        int initialPuzzlePieceX;
        do {
            initialPuzzlePieceX = random.nextInt(maxPossibleX + 1);
        } while (Math.abs(initialPuzzlePieceX - correctX) < (PUZZLE_PIECE_WIDTH / 2) && maxPossibleX > (PUZZLE_PIECE_WIDTH / 2));

        return new CaptchaResDto(holedImageBase64, puzzlePieceBase64,
                PUZZLE_PIECE_WIDTH, PUZZLE_PIECE_HEIGHT,
                initialPuzzlePieceX, puzzleY,
                originalImage.getWidth(), originalImage.getHeight(),
                correctX
        );
    }

    public boolean verifyPuzzle(int userPuzzlePieceX, HttpSession session) {
        Integer correctX = (Integer) session.getAttribute("captchaCorrectX");
        Integer puzzleY = (Integer) session.getAttribute("captchaPuzzleY");

        session.removeAttribute("captchaCorrectX");
        session.removeAttribute("captchaPuzzleY");

        if (correctX == null) {
            System.out.println("No CAPTCHA solution found in session (correctX is null).");
            return false;
        }

        boolean isCorrect = Math.abs(userPuzzlePieceX - correctX) <= TOLERANCE;
        System.out.println("Backend Verification: User X: " + userPuzzlePieceX + ", Correct X: " + correctX + ", Is Correct: " + isCorrect + " (Tolerance: " + TOLERANCE + ")");
        return isCorrect;
    }

    private String encodeImageToBase64(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}