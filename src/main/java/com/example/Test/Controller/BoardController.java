package com.example.Test.Controller;

import com.example.Test.Service.BoardService;
import com.example.Test.model.Board;
import com.example.Test.repository.BoardRepository;
import com.example.Test.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model,@PageableDefault(size = 5) Pageable pageable,
                       @RequestParam(required = false,defaultValue = "") String searchText) {
//        Page<Board> boards = boardRepository.findAll(pageable);
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("boards", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "board/list";
    }

    @RequestMapping(value = "/list1", produces = "application/json; charset=utf8")
    public @ResponseBody
    List boardlist() {
        List<Board> boards = boardRepository.findAll();
        System.out.println(boards.get(0).toString());
        return boards;
    }

    @GetMapping("/write")
    public String write() {
        return "board/write";
    }

    @PostMapping("/write")
    public String write1(Board board, Model model) {
        boardRepository.save(board);
        return "redirect:/board/list";
    }

    @GetMapping("/info")
    public String info(@RequestParam("id") Long id, Model model) {
        Optional<Board> byId = boardRepository.findById(id).stream().findAny();
        model.addAttribute("con", byId);
        return "board/info";
    }

    @GetMapping("/form")
    public String form(Model model,@RequestParam(required = false) Long id)  {
        if (id == null){
            model.addAttribute("board", new Board());
        }else {
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }
        return "board/form";
    }

    @PostMapping("/form")
    public String postForm(@Valid Board board, BindingResult bindingResult, Authentication authentication) {
        boardValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            return "board/form";
        }
        String username = authentication.getName();
        boardService.save(username, board);
        return "redirect:/board/list";
    }

}
