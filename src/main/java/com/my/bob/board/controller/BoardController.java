package com.my.bob.board.controller;

import com.my.bob.board.dto.*;
import com.my.bob.board.service.BoardConvertService;
import com.my.bob.board.service.BoardDeleteService;
import com.my.bob.board.service.BoardSaveService;
import com.my.bob.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardSaveService boardSaveService;
    private final BoardConvertService boardConvertService;
    private final BoardDeleteService boardDeleteService;

    @GetMapping
    public ResponseEntity<ResponseDto<Page<BoardTitleDto>>> getBoardList(@ModelAttribute BoardSearchDto dto,
                                                                         Pageable pageable){

        Page<BoardTitleDto> pageDtoList = boardConvertService.convertBoardList(dto, pageable);
        return ResponseEntity.ok(new ResponseDto<>(pageDtoList));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Long>> addBoard(@RequestBody BoardCreateDto dto) {
        long boardId = boardSaveService.saveNewBoard(dto);

        return ResponseEntity.ok(new ResponseDto<>(boardId));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseDto<BoardDto>> getBoard(@PathVariable long boardId) {
        BoardDto dto = boardConvertService.convertBoardDto(boardId);

        return ResponseEntity.ok(new ResponseDto<>(dto));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(@PathVariable long boardId,
                                      @RequestBody BoardUpdateDto dto, Principal principal) {
        String userName = principal.getName();
        boardSaveService.updateBoard(boardId, userName, dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable long boardId, Principal principal) {
        String requestUser = principal.getName();
        boardDeleteService.deleteBoard(boardId, requestUser);

        return ResponseEntity.noContent().build();
    }


    /* 댓글 */
    @PostMapping("/{boardId}/comment")
    public ResponseEntity<Void> addComment(@PathVariable long boardId,
                                     @RequestBody BoardCommentCreateDto dto) {
        boardSaveService.saveNewComment(boardId, dto);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable long commentId,
                                        @RequestBody BoardCommentUpdateDto dto, Principal principal) {
        String requestUser = principal.getName();
        boardSaveService.updateComment(commentId, requestUser, dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId, Principal principal) {
        String requestUser = principal.getName();
        boardDeleteService.deleteComment(commentId, requestUser);

        return ResponseEntity.noContent().build();
    }

}
