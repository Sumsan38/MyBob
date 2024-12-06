package com.my.bob.v1.board.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.board.dto.BoardSearchDto;
import com.my.bob.core.domain.board.entity.Board;
import com.my.bob.core.service.board.BoardService;
import com.my.bob.v1.board.repository.BoardQueryRepository;
import com.my.bob.v1.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;

    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    public Board getById(long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.NON_EXISTENT_POST));
    }

    public Page<Board> getBySearch(BoardSearchDto dto, Pageable pageable) {
        return boardQueryRepository.getBoardList(dto, pageable);
    }
}