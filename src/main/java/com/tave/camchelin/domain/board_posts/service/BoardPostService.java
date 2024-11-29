package com.tave.camchelin.domain.board_posts.service;

import com.tave.camchelin.domain.board_posts.dto.BoardPostDto;
import com.tave.camchelin.domain.board_posts.entity.BoardPost;
import com.tave.camchelin.domain.board_posts.repository.BoardPostRepository;
import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.communities.repository.CommunityRepository;
import com.tave.camchelin.domain.users.entity.User;
import com.tave.camchelin.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardPostService {
    private static BoardPostRepository boardPostRepository;
    private static UserRepository userRepository;
    private static CommunityRepository communityRepository;

    public List<BoardPostDto> getBoardPosts() {
        List<BoardPost> boardPosts = boardPostRepository.findAll();
        return boardPosts.stream()
                .map(BoardPostDto::fromEntity)
                .collect(Collectors.toList());
    }

    public BoardPostDto getBoardPostById(Long boardPostId) {
        BoardPost boardPost = boardPostRepository.findById(boardPostId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return BoardPostDto.fromEntity(boardPost);
    }

    public void writeBoardPost(BoardPostDto boardPostDto) {
        // User와 Community를 찾아 연관 관계를 설정
        User user = userRepository.findById(boardPostDto.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        Community community = communityRepository.findByName(boardPostDto.getCommunity().getName())
                .orElseThrow(() -> new IllegalArgumentException("커뮤니티 정보를 찾을 수 없습니다."));

        // BoardPost Entity 생성 및 저장
        BoardPost boardPost = BoardPost.builder()
                .title(boardPostDto.getTitle())
                .content(boardPostDto.getContent())
                .user(user)
                .community(community)
                .build();

        boardPostRepository.save(boardPost);
    }

    public void editBoardPost(Long boardPostId, BoardPostDto boardPostDto) {
        BoardPost boardPost = boardPostRepository.findById(boardPostId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 수정 가능한 필드만 업데이트
        boardPost.edit(boardPostDto.getTitle(), boardPostDto.getContent());

        boardPostRepository.save(boardPost);
    }

    public void deleteBoardPost(Long boardPostId) {
        BoardPost boardPost = boardPostRepository.findById(boardPostId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        boardPostRepository.delete(boardPost);
    }
}
