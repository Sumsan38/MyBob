package com.my.bob.board.service;

import com.my.bob.board.dto.BoardCommentDto;
import com.my.bob.board.dto.BoardDto;
import com.my.bob.board.dto.BoardSearchDto;
import com.my.bob.board.dto.BoardTitleDto;
import com.my.bob.board.entity.Board;
import com.my.bob.board.entity.BoardComment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardConvertService {

    private final BoardService boardService;

    private final ModelMapper modelMapper;


    public BoardDto convertBoardDto(long boardId) {
        Board board = boardService.getById(boardId);

        BoardDto dto = new BoardDto();
        dto.setBoardId(boardId);
        dto.setTitle(board.getBoardTitle());
        dto.setContent(board.getBoardContent());
        dto.setDelete(board.isDelete());

        // later board 조회할 때 comments 함께 들고오도록 변경할 것
        List<BoardComment> comments = board.getRootComments();
        dto.setCommentList(convertCommentList(comments));

        return dto;
    }

    public Page<BoardTitleDto> convertBoardList(BoardSearchDto dto, Pageable pageable) {
        return boardService.getBySearch(dto, pageable).map(this::convertTitleDto);
    }

    /* private */
    private BoardTitleDto convertTitleDto(Board board) {
        if (board.isDelete()) {
            BoardTitleDto dto = new BoardTitleDto();
            dto.setBoardTitle("삭제된 글입니다.");
            return dto;
        }

        BoardTitleDto dto = modelMapper.map(board, BoardTitleDto.class);

        String regDateStr = board.getRegDate().toLocalDate().toString();
        dto.setRegDate(regDateStr);

        return dto;
    }


    private List<BoardCommentDto> convertCommentList(List<BoardComment> boardComments) {
        if (CollectionUtils.isEmpty(boardComments)) {
            return Collections.emptyList();
        }

        return boardComments
                .stream()
                .map(this::convertCommentDto)
                .toList();
    }

    private BoardCommentDto convertCommentDto(BoardComment boardComment) {
        BoardCommentDto commentDto = new BoardCommentDto();
        commentDto.setCommentId(boardComment.getCommentId());
        commentDto.setContent(boardComment.getCommentContent());
        commentDto.setDelete(boardComment.isDelete());

        List<BoardComment> childComments = boardComment.getChildComments();
        commentDto.setSubComments(convertCommentList(childComments));

        return commentDto;
    }
}
