package com.buybike.app.controller;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.BoardFormDto;
import com.buybike.app.domain.Member;
import com.buybike.app.service.BoardService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

//    @GetMapping
//    public String requestBoards(Model model) {
//        List<Board> list = boardService.getAllBoardList();
//        model.addAttribute("boardList", list);
//        return "boards";
//    }
//
//    @GetMapping("/all")
//    public ModelAndView requestAllBoards(){
//        ModelAndView modelAndView = new ModelAndView();
//        List<Board> list = boardService.getAllBoardList();
//        modelAndView.addObject("boardList", list);
//        modelAndView.setViewName("boards");
//        return modelAndView;
//    }
//
//    @GetMapping("/board")
//    public String requestBoardById(@RequestParam("id") Long boardId, Model model) {
//        Board board = boardService.getBoardById(boardId);
//        model.addAttribute("board", board);
//        return "board";
//    }
//
//    @Value("${file.uploadDir}")
//    String fileDir;
//
//    @GetMapping("/add")
//    public String requestAddBoard() {
//        return "addBoard";
//    }
//
//    @PostMapping("/add")
//    public String submitAddNewBoard(@Valid @ModelAttribute Board board, BindingResult bindingResult) {
//        if( bindingResult.hasErrors()) {
//            return "addBoard";
//        }
//
//        MultipartFile boardImage = board.getBoardImage();
//        String saveName = boardImage.getOriginalFilename();
//        File saveFile = new File(fileDir, saveName);
//        if (boardImage != null && !boardImage.isEmpty()) {
//            try {
//                boardImage. transferTo(saveFile);
//            } catch (Exception e) {
//                throw new RuntimeException("게시판 이미지 업로드가 실패하였습니다.", e);
//            }
//        }
//        board.setFileName(saveName);
//        boardService.setNewBoard(board);
//        return "redirect:/boards";
//    }
//
//    @GetMapping("/download")
//    public void downloadBoardImage(@RequestParam("file") String paramKey, HttpServletResponse response) throws IOException {
//        if (paramKey == null) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//
//        File imageFile = new File(fileDir + paramKey );
//
//        if (imageFile.exists() == false) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            return;
//        }
//
//        response.setContentType("application/download");
//        response.setContentLength((int)imageFile.length());
//        response.setHeader("Content-Disposition", "attachment;filename=\"" + paramKey + "\"");
//        ServletOutputStream os = response.getOutputStream();
//        FileInputStream fis = new FileInputStream((imageFile));
//        FileCopyUtils.copy(fis, os);
//        fis.close();
//        os.close();
//    }
//    @ModelAttribute
//    public void addAttributes(Model model) {
//        model.addAttribute("addTitle", "새 게시판 글 작성");
//    }
//
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.setAllowedFields("boardId", "title", "content", "user", "regDt", "modDt", "boardImage");
//    }
//
//


    // 전체 게시글 목록 가져오기 (1페이지, id 내림차순 기본)
    @GetMapping("/list")
    public String viewHomePage(Model model) {
        return viewPage(1, "id", "desc", model);
    }

    // 페이징 및 정렬된 게시글 목록 가져오기
    @GetMapping("/page")
    public String viewPage(@RequestParam("pageNum") int pageNum,
                           @RequestParam("sortField") String sortField,
                           @RequestParam("sortDir") String sortDir,
                           Model model) {
        int pageSize = 10; // 한 페이지에 보여줄 게시글 수
        List<Board> listBoard = boardService.listAll(pageNum, pageSize, sortField, sortDir);
        int totalItems = boardService.getTotalBoardCount();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("boardList", listBoard);
        return "board/list";
    }

    // 게시글 글쓰기 페이지 출력
    @GetMapping("/write")
    public String post() {
        return "board/write";
    }

    // 게시글 글쓰기 저장
    @PostMapping("/write")
    public String write(BoardFormDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/board/list";
    }

    // 게시글 상세 보기
    @GetMapping("/view/{boardId}")
    public String requestUpdateMemberForm(@PathVariable(name = "boardId") Long boardId,
                                          HttpServletRequest httpServletRequest, Model model) {
        Board board = boardService.getBoardById(boardId);
        model.addAttribute("boardFormDto", board);
        HttpSession session = httpServletRequest.getSession(true);
        Member member = (Member) session.getAttribute("userLoginInfo");
        model.addAttribute("buttonOk", false);
        if (member != null && board.getMemberId().equals(member.getMemberId())) {
            model.addAttribute("buttonOk", true);
        }
        return "board/view";
    }

    // 게시글 수정
    @PostMapping("/update")
    public String submitUpdateMember(@Valid BoardFormDto boardDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "board/view";
        try {
            boardService.updateBoard(boardDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "board/view";
        }
        return "redirect:/board/list";
    }

    // 게시글 삭제
    @GetMapping("/delete/{boardId}")
    public String deleteOrder(@PathVariable(name = "boardId") Long boardId) {
        boardService.deleteBoardById(boardId);
        return "redirect:/board/list";
    }

    @GetMapping("")
    public ResponseEntity<T> getListBoard(Board board, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(boardService.getListBoard(board, pageable));
    }
}



