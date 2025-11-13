package com.example.gifUploader.service;

import com.example.gifUploader.domain.Gif;
import com.example.gifUploader.repository.GifRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

@Service
@Transactional
public class GifService {
    @Getter
    @Value("${app.upload.root}")
    private String uploadRoot;

    private final GifRepository gifRepository;

    public GifService(GifRepository gifRepository) {
        this.gifRepository = gifRepository;
    }

    public void uploadGif(MultipartFile file, String gifName,
                          String userName, String password, String tags) throws Exception {
        List<String> tagList = Arrays.stream(tags.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        String gifTags = String.join(",", tagList);

        System.out.println("UPLOAD_ROOT = " + uploadRoot);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        String passwordHash = HexFormat.of().formatHex(hash);

        String originalName = file.getOriginalFilename(); // 예: "cat.gif"
        String ext = originalName.substring(originalName.lastIndexOf(".")); // ".gif"
        if (!ext.equalsIgnoreCase(".gif")) {
            throw new IllegalArgumentException("GIF 파일만 업로드 가능합니다.");
        }

        String storedName = System.currentTimeMillis() + "_" + gifName + ext;

        Path root = Path.of(uploadRoot).toAbsolutePath().normalize();

        Files.createDirectories(root);

        Path dest = root.resolve(storedName).normalize();
        System.out.println("DEST = " + dest);

        Files.copy(file.getInputStream(), dest);

        long sizeBytes = file.getSize();

        Gif gif = new Gif(
                gifName,
                storedName,
                tags,
                userName,
                passwordHash,
                sizeBytes
        );

        gifRepository.save(gif);
    }

    public Page<Gif> searchPaged(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10); // 1페이지당 10개
        String q = keyword.trim().toLowerCase();
        return gifRepository.findByGifNameContainingIgnoreCaseOrGifTagsContainingIgnoreCase(q, q, pageable);
    }

    @Transactional
    public void deleteGif(Long id, String password) {
        Gif gif = gifRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다."));

        // 입력 비밀번호 해시화
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) { throw new RuntimeException(e); }

        String inputHash = HexFormat.of().formatHex(md.digest(password.getBytes(StandardCharsets.UTF_8)));

        // 비밀번호 검증
        if (!gif.getUserPasswordHash().equals(inputHash)) {
            throw new IllegalArgumentException("password mismatch");
        }

        // 실제 파일 삭제
        Path root = Path.of(uploadRoot).toAbsolutePath().normalize();
        Path file = root.resolve(gif.getStoredName());

        try {
            Files.deleteIfExists(file);
        } catch (Exception ignored) {}

        // DB 레코드 삭제
        gifRepository.delete(gif);
    }

}
