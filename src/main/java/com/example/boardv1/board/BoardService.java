package com.example.boardv1.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.boardv1.user.User;

import lombok.RequiredArgsConstructor;

// 책임 : 트랜잭션관리, DTO 만들기, 권한체크(DB 정보가 필요하니까까)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<Board> 게시글목록() {
        return boardRepository.findAll();
    }

    public Board 수정폼게시글정보(int id, int sessionUserId) {

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id로 게시글을을 찾을수 없어요"));

        // 권한
        if (sessionUserId != board.getUser().getId())
            throw new RuntimeException("권한이 없습니다.");

        return board;
    }

    public BoardResponse.DetailDTO 상세보기(int id, Integer sessionUserId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id로 게시글을을 찾을수 없어요"));

        return new BoardResponse.DetailDTO(board, sessionUserId);

    }

    @Transactional // update, delete, insert 할때 붙이세요!!
    public void 게시글수정(int id, String title, String content, int sessionUserId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id로 게시글을 찾을수 없어요"));

        if (sessionUserId != board.getUser().getId())
            throw new RuntimeException("권한이 없습니다.");

        // User user = (User) board.getUser();

        board.setTitle(title);
        board.setContent(content);
    }

    // 원자성(모든게 다되면 commit,하나라도 실패하면 rollback)
    // 트랜잭션 종료시 flush 됨.
    @Transactional
    public void 게시글쓰기(String title, String content, User sessionUser) {
        // 비영속객체체
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setUser(sessionUser);
        // persist
        boardRepository.save(board);
    }

    @Transactional
    public void 게시글삭제(int id, int sessionUserId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id로 게시글을 찾을수 없어요"));

        if (sessionUserId != board.getUser().getId())
            throw new RuntimeException("삭제할 권한이 없습니다.");

        boardRepository.delete(board);
    }

}
