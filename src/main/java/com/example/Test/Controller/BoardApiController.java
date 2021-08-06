package com.example.Test.Controller;

import com.example.Test.model.Board;
import com.example.Test.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class BoardApiController {
    @Autowired
    private BoardRepository boardRepository;

    @GetMapping(value = "/boards", produces = "application/json; charset=utf8")
    List<Board> all(@RequestParam(required = false, defaultValue = "") String title,
                    @RequestParam(required= false,defaultValue = "") String content) {
        if (StringUtils.isEmpty(title)&& StringUtils.isEmpty(content)) {
            return boardRepository.findAll();
        }else{
            return boardRepository.findByTitleOrContent(title,content);
        }
    }

    @PostMapping("/boards")
    Board newBoard(@RequestBody Board newBoard) {
        return boardRepository.save(newBoard);
    }

    @GetMapping("/boards/{id}")
    Board one(@PathVariable Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}")
    Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {
        return boardRepository.findById(id)
                .map(board -> {
                    board.setTitle(newBoard.getTitle());
                    board.setContent(newBoard.getContent());
                    return boardRepository.save(board);
                })
                .orElseGet(() -> {
                    newBoard.setId(id);
                    return boardRepository.save(newBoard);
                });
    }
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/boards/{id}")
    void deleteBoard(@PathVariable Long id) {
        boardRepository.deleteById(id);
    }
}
