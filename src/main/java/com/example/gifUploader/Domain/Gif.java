package com.example.gifUploader.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
public class Gif {
    @Id // 이 필드 변수가 db 내부에서 고유한 값을 가진 id로 사용함을 의미하는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 데이터를 DB에 저장할 때 id가 1씩 자동으로 증가하여 저장된다(맨처음 저장되는 gif는 0부터 시작)
    private Long id;
    private String gifName; // gif 이름
    private String storedFilename;
    private String userName; // 사용자 이름
    @Getter(AccessLevel.NONE)
    private String userPasswordHash; // 비번 암호화한 해시 형태로 저장
    @ElementCollection
    private List<String> gifTags; // gif 태그들(구분자 쉼표)
    private Instant uploadTime;
    private Long fileSize;

    public Gif(String gifName, String gifTags,
               String userName, String userPasswordHash) {
        validate_allInput(gifName, gifTags, userName, userPasswordHash);
        this.gifName = gifName;
        this.userName = userName;
        this.userPasswordHash = userPasswordHash;
        this.gifTags = makegifTagsList(gifTags);
    }

    // JPA 전용 protected 디폴트 생성자
    protected Gif() {}

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

    private List<String> makegifTagsList(String gifTags) {
        String term = gifTags.replaceAll(" ", "");
        return new ArrayList<>(Arrays.asList(term.split(",")));
    }

    public boolean comparePassword(String password) {
        return Objects.equals(this.userPasswordHash, password);
    }
}
