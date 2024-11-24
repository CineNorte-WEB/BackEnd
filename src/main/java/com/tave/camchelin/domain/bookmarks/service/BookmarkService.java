package com.tave.camchelin.domain.bookmarks.service;

import com.tave.camchelin.domain.bookmarks.entity.Bookmark;
import com.tave.camchelin.domain.bookmarks.repository.BookmarkRepository;
import com.tave.camchelin.domain.places.dto.PlaceDto;
import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.places.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.support.PlaceholderResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    public List<PlaceDto> getUserBookmarks(Long userId) {
        // User가 북마크한 모든 Bookmark 조회
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);

        // 북마크한 Place 목록을 Dto 형태로 변환하여 반환
        return bookmarks.stream()
                .map(bookmark -> PlaceDto.fromEntity(bookmark.getPlace())) // PlaceDto로 변환
                .collect(Collectors.toList());
    }
}
