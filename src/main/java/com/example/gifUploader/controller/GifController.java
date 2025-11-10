package com.example.gifUploader.controller;

import com.example.gifUploader.domain.Gif;
import com.example.gifUploader.service.GifService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class GifController {
    private final GifService gifService;

    public GifController(GifService gifService) {
        this.gifService = gifService;
    }

    @PostMapping("/upload")
    public String uploadGif(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("tags") String tags
            ) throws Exception {
        Gif gif = gifService.uploadGif(file, userName, password, tags);

        return "업로드 완료. 접근 링크: http://localhost:8080/files/" + gif.getStoredName();
    }
    // @RequestParam 옆 종류들:
    // 프론트에서 입력값 받을때 정해진 타입대로 매핑되어 함수 인자로 전달되는 것
}
