package com.tave.camchelin.domain.board_posts.service;

import com.tave.camchelin.domain.board_posts.dto.BoardPostDto;
import com.tave.camchelin.domain.board_posts.dto.response.ResponseBoardDto;
import com.tave.camchelin.domain.board_posts.dto.request.UpdateRequestBoardDto;
import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.board_posts.repository.BoardPostRepository;
import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.communities.repository.CommunityRepository;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardPostService {
    private final BoardPostRepository boardPostRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    @Transactional(readOnly = true)
    public List<ResponseBoardDto> getBoardPosts() {
        List<BoardPost> boardPosts = boardPostRepository.findAll();
        return boardPosts.stream()
                .map(ResponseBoardDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseBoardDto getBoardPostById(Long boardPostId) {
        BoardPost boardPost = boardPostRepository.findById(boardPostId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return ResponseBoardDto.fromEntity(boardPost);
    }

    @Transactional
    public ResponseBoardDto writeBoardPost(Long userId, BoardPostDto boardPostDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        Community community = communityRepository.findByName("boardPost")
                .orElseThrow(() -> new IllegalArgumentException("커뮤니티 정보를 찾을 수 없습니다."));

        BoardPost boardPost = boardPostDto.toEntity(user, community);

        boardPost = boardPostRepository.save(boardPost);
        return ResponseBoardDto.fromEntity(boardPost);
    }

    @Transactional
    public void editBoardPost(Long userId, Long boardPostId, UpdateRequestBoardDto updateRequestDto) {
        BoardPost boardPost = boardPostRepository.findById(boardPostId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!boardPost.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        boardPost.edit(
                updateRequestDto.getTitle(),
                updateRequestDto.getContent()
        );
    }

    @Transactional
    public void deleteBoardPost(Long userId, Long boardPostId) {
        BoardPost boardPost = boardPostRepository.findById(boardPostId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!boardPost.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        boardPostRepository.delete(boardPost);
    }
}
