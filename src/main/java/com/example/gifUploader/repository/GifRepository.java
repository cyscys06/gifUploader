package com.example.gifUploader.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gifUploader.domain.Gif;

public interface GifRepository extends JpaRepository<Gif, Long> {
    // gif 이름으로 조회하는 인터페이스
    Page<Gif> findByGifNameContaining(String gifName, Pageable pageable);

}
