package com.example.gifUploader.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
public class Gif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gifName; // gif 이름

    @Column(nullable = false, unique = true)
    private String storedName; // DB에 저장되는 이름

    @Column(nullable = false)
    private String userName; // 사용자 이름

    @Column(nullable = false)
    private String userPasswordHash; // 비번 암호화한 해시 형태로 저장

    private String gifTags; // gif 태그들(구분자 쉼표)

    @Column(nullable = false)
    private LocalDateTime uploadTime = LocalDateTime.now(); // 파일 업로드 당시 시간으로 저장

    @Column(nullable = false)
    private Long fileSize; // 파일 크기

    protected Gif() {}

    public Gif(String gifName, String storedName,
               String gifTags, String userName,
               String userPasswordHash, long fileSize) {
        validate_allInput(gifName, gifTags, userName, userPasswordHash);

        this.gifName = gifName;
        this.storedName = storedName;
        this.gifTags = gifTags;
        this.userName = userName;
        this.userPasswordHash = userPasswordHash;
        this.fileSize = fileSize;
    }

    private void validate_emptyString(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("빈 문자열을 입력할 수 없습니다.");
        }
    }

    private void validate_allInput(String gifName, String gifTags,
                                   String userName, String userPasswordHash) {
        validate_emptyString(gifName);
        validate_emptyString(gifTags);
        validate_emptyString(userName);
        validate_emptyString(userPasswordHash);
    }

    public List<String> getTagList() {
        if (gifTags == null || gifTags.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(gifTags.split(","))
                .map(String::trim)
                .toList();
    }

}
