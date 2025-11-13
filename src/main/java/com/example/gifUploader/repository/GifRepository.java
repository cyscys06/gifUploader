package com.example.gifUploader.repository;

import com.example.gifUploader.domain.Gif;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GifRepository extends JpaRepository<Gif, Long> {
    Page<Gif> findByGifNameContainingIgnoreCaseOrGifTagsContainingIgnoreCase(
            String name, String tags, Pageable pageable);
}

