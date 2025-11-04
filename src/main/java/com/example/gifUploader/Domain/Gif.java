package com.example.gifUploader.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.Instant;

@Entity
public class Gif {
    @id // 이 필드 변수가 db 내부에서 고유한 값을 가진 id로 사용함을 의미하는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 데이터를 DB에 저장할 때 id가 1씩 자동으로 증가하여 저장된다(맨처음 저장되는 gif는 0부터 시작)
    private Long id;
    private final String originalName; // gif 이름
    private String storedFilename;
    private final String uploaderName; // 사용자 이름
    private final String passwordHash; // 비번 암호화한 해시 형태로 저장
    private final String tags; // gif 태그들(구분자 쉼표)
    private Instant uploadTime;
    private Long size;

    public Gif(String originalName, String uploaderName,
               String passwordHash, String tags) {
        this.originalName = originalName;
        this.uploaderName = uploaderName;
        this.passwordHash = passwordHash;
        this.tags = tags;
    }

    private void validate_emptyString(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("빈 문자열을 입력할 수 없습니다.");
        }
    }
}
