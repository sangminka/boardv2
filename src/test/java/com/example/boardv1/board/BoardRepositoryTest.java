package com.example.boardv1.board;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.persistence.EntityManager;

@Import(BoardRepository.class)
@DataJpaTest // EntityManager가 ioc에 등록됨
public class BoardRepositoryTest {
    @Autowired // 어노테이션 DI 기법.
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void save_test() {
        // given
        Board board = new Board();
        board.setTitle("title7");
        board.setContent("content7");
        System.out.println("=== before persist====");
        System.out.println(board);
        // when
        boardRepository.save(board);
        // eye (board 객체가 DB 데이터와 동기화 되었음.)
        System.out.println("=== after persist====");
        System.out.println(board);
    }

    @Test
    public void findById_test() {
        // given
        int id = 1;
        // when
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id로 찾을수 없어요"));
        // boardRepository.findById(1);
        // eye
        System.out.println(board);

    }

    @Test
    public void findByIdV2_test() {
        // given
        int id = 1;
        // when
        boardRepository.findById(id);
        em.clear();
        boardRepository.findById(id);

    }

    @Test
    public void findAll_test() {
        // given

        // when
        List<Board> list = boardRepository.findAll();

        // eye
        for (Board board : list) {
            System.out.println(board);
        }

    }

    @Test
    public void delete_test() {
        // given
        int id = 1;
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id로 찾을수 없어요")); // when
        boardRepository.delete(board);

        // eye
        em.flush();

    }

    @Test
    public void update_test() {
        // given
        int id = 1;
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id로 찾을수 없어요")); // when
        boardRepository.delete(board);
        // when
        board.setTitle("title-update");
        // eye
        em.flush();

        // when
        List<Board> list = boardRepository.findAll();

        // eye
        for (Board board2 : list) {
            System.out.println(board2);
        }
    }

    @Test
    public void orm_test() {
        int id = 1;

        Board board = boardRepository.findById(id).get();

        System.out.println("board -> user -> id: " + board.getUser().getId());
        System.out.println("================================================");
        System.out.println("board -> user -> username: " + board.getUser().getUsername());
        System.out.println(board);
    }

}
