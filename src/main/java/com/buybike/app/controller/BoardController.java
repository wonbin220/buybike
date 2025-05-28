package com.buybike.app.controller;

import com.buybike.app.domain.Board;
import com.buybike.app.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/boards")
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping
    public String requestBoards(Model model) {
        List<Board> list = boardService.getAllBoardList();
        model.addAttribute("boardList", list);
        return "boards";
    }

    @GetMapping("/all")
    public ModelAndView requestAllBoards(){
        ModelAndView modelAndView = new ModelAndView();
        List<Board> list = boardService.getAllBoardList();
        modelAndView.addObject("boardList", list);
        modelAndView.setViewName("boards");
        return modelAndView;
    }
}
