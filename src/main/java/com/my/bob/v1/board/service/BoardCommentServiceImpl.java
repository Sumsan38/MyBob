package com.my.bob.v1.board.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.board.entity.BoardComment;
import com.my.bob.core.service.board.BoardCommentService;
import com.my.bob.v1.board.repository.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentServiceImpl implements BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;

    @Transactional
    public void save(BoardComment boardComment) {
        boardCommentRepository.save(boardComment);
    }

    public BoardComment getById(long commentId) {
        return boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST));
    }

}