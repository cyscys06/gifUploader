package com.example.gifUploader.service;

import com.example.gifUploader.domain.Gif;
import com.example.gifUploader.repository.GifRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;

@Service
// Repository에서 갖고온 정보를 Controller에 넘겨주는 역할
// 그런 역할을 하도록 이 클래스를 빈에 등록함
// -> 스프링 자체에서 이 클래스가 Service임을 인식하게 하기
@Transactional
// 트랜잭션: DB 상태를 변화시키는 최소 단위 연산
// 동시에 여러개의 연산을 하면 변경사항이 충돌할 수 있음(커밋 충돌처럼)
// 이러한 문제 방지를 위해 하나의 연산은 독립적으로 처리되도록 함
public class GifService {
    @Value("${app.upload.root}")
    private String uploadRoot;

    private final GifRepository gifRepository;

    public GifService(GifRepository gifRepository) {
        this.gifRepository = gifRepository;
    }

    public Gif uploadGif(MultipartFile file, String userName,
                         String password, String tags) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        String passwordHash = HexFormat.of().formatHex(hash);

        String gifName = file.getOriginalFilename();
        String storedName = System.currentTimeMillis() + "_" + gifName;

        Path root = Path.of(uploadRoot).toAbsolutePath().normalize();
        Files.createDirectories(root);
        Path dest = root.resolve(storedName).normalize();

        Files.copy(file.getInputStream(), dest);

        long sizeBytes = file.getSize();

        Gif gif = new Gif(
                gifName,
                tags,
                userName,
                passwordHash,
                storedName,
                sizeBytes
        );

        return gifRepository.save(gif);
    }
}
