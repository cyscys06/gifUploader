package com.example.gifUploader.controller;

import com.example.gifUploader.domain.Gif;
import com.example.gifUploader.service.GifService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;

@Controller
public class GifController {
    private final GifService gifService;

    public GifController(GifService gifService) {
        this.gifService = gifService;
    }

    @GetMapping("/upload-page")
    public String uploadPage() {
        return "form"; // form.html 파일 이름과 동일
    }

    @PostMapping("/uploads")
    public String uploadGif(
            @RequestParam("file") MultipartFile file,
            @RequestParam("gifName") String gifName,
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("tags") String tags
            ) throws Exception {
        gifService.uploadGif(file, gifName, userName, password, tags);

        return "redirect:/search-page";
    }

    @GetMapping("/search-page")
    public String searchPage() {
        return "search"; // search.html 반환
    }

    @GetMapping("/search-result")
    public String searchResult(@RequestParam("q") String keyword,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               Model model) {

        Page<Gif> pageResult = gifService.searchPaged(keyword, page);

        model.addAttribute("results", pageResult.getContent());
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "search-result";
    }

    @PostMapping("/delete")
    public String deleteGif(@RequestParam("id") Long id,
                            @RequestParam("password") String password,
                            @RequestParam("q") String keyword,
                            RedirectAttributes ra) {

        try {
            gifService.deleteGif(id, password);
            ra.addFlashAttribute("msg", "삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
        }

        return "redirect:/search-result?q=" + keyword;
    }

    @GetMapping("/download/{storedName}")
    public ResponseEntity<Resource> download(@PathVariable String storedName) throws Exception {

        Path file = Path.of(gifService.getUploadRoot()).resolve(storedName).normalize();
        Resource resource = new UrlResource(file.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + storedName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
