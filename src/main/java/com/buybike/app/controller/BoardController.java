package com.buybike.app.controller;

import com.buybike.app.domain.Board;
import com.buybike.app.service.BoardService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    @GetMapping("/board")
    public String requestBoardById(@RequestParam("id") Long boardId, Model model) {
        Board board = boardService.getBoardById(boardId);
        model.addAttribute("board", board);
        return "board";
    }

    @Value("${file.uploadDir}")
    String fileDir;

    @GetMapping("/add")
    public String requestAddBoard() {
        return "addBoard";
    }

    @PostMapping("/add")
    public String submitAddNewBoard(@Valid @ModelAttribute Board board, BindingResult bindingResult) {
        if( bindingResult.hasErrors()) {
            return "addBoard";
        }

        MultipartFile boardImage = board.getBoardImage();
        String saveName = boardImage.getOriginalFilename();
        File saveFile = new File(fileDir, saveName);
        if (boardImage != null && !boardImage.isEmpty()) {
            try {
                boardImage. transferTo(saveFile);
            } catch (Exception e) {
                throw new RuntimeException("게시판 이미지 업로드가 실패하였습니다.", e);
            }
        }
        board.setFileName(saveName);
        boardService.setNewBoard(board);
        return "redirect:/boards";
    }

    @GetMapping("/download")
    public void downloadBoardImage(@RequestParam("file") String paramKey, HttpServletResponse response) throws IOException {
        if (paramKey == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        File imageFile = new File(fileDir + paramKey );

        if (imageFile.exists() == false) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/download");
        response.setContentLength((int)imageFile.length());
        response.setHeader("Content-Disposition", "attachment;filename=\"" + paramKey + "\"");
        ServletOutputStream os = response.getOutputStream();
        FileInputStream fis = new FileInputStream((imageFile));
        FileCopyUtils.copy(fis, os);
        fis.close();
        os.close();
    }
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("addTitle", "새 게시판 글 작성");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("boardId", "title", "content", "user", "regDt", "modDt", "boardImage");
    }



}
