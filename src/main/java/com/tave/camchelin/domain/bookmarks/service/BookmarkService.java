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
    public boolean isBookmarkExist(Long userId, Long placeId) {
        // 이미 해당 가게가 북마크된 상태인지 확인
        return bookmarkRepository.existsByUserIdAndPlaceId(userId, placeId);
    }
}
