package com.buybike.app.controller;

import com.buybike.app.domain.Board;
import com.buybike.app.domain.BoardFormDto;
import com.buybike.app.domain.Member;
import com.buybike.app.domain.Pagination;
import com.buybike.app.service.BoardService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
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

    @GetMapping("/list")
    public String list(Model model, Pagination pagination) throws Exception {
        int page = (int) pagination.getPage();
        int size = (int) pagination.getSize();
        PageInfo<Board> pageInfo = boardService.page(page, size);
        log.info("pageInfo: {}", pageInfo);
        model.addAttribute("pageInfo", pageInfo);

        // Uri 빌더
        String pageUri = UriComponentsBuilder.fromPath("/board/list")
                .queryParam("size", pageInfo.getSize())
                .queryParam("count", pageInfo.getPageSize())
                .build()
                .toUriString();

        model.addAttribute("pageUri", pageUri);
        log.info("pageUri: {}", pageUri);
        return "board/list";
    }

    // 게시글 조회 화면
    @GetMapping("/view/{no}")
    public String view(@PathVariable("no") Integer no, Model model) throws Exception {
        // 데이터 요청
        Board board = boardService.select(no);
        // 모델 등록
        model.addAttribute("board", board);
        return "board/view";
    }

    // 게시글 등록
    @GetMapping("/create")
    public String create(@ModelAttribute(value = "boardFormDto") BoardFormDto boardFormDto) {
        return "board/create";
    }

    @PostMapping(path="/create")
    public String createPostForm(BoardFormDto boardFormDto) throws Exception {
        // 데이터 요청
        boolean result = boardService.insert(boardFormDto.toEntity());
        // 리다이렉트
        // 데이터 처리 성공
        if (result)
            return "redirect:/board/list";
        return "redirect:/board/create?error=true";
    }
//
//    @PostMapping("/create")
//    public String createPostForm(@Valid BoardFormDto boardFormDto, Model model) {
//        try {
//            Board board = boardFormDto.toEntity();
//            boardService.insert(board);
//            return "redirect:/boards/list";
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "게시글 등록에 실패했습니다.");
//            return "board/create";
//        }
//    }


    // 회원 정보 수정 페이지 출력하기
    @GetMapping("/update/{no}")
    public String update(@PathVariable("no") Integer no, Model model) throws Exception {
        // 데이터 요청
        Board board = boardService.select(no);
        // 모델 등록
        model.addAttribute("board", board);
        return "board/update";
    }



    @PostMapping("/update")
    public String updatePostForm(@Valid BoardFormDto boardFormDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시, 다시 수정 폼으로 돌아감
            return "board/update";
        }

        // 데이터 요청
        boolean result = boardService.update(boardFormDto);

        // 데이터 처리 성공
        if (result) {
            return "redirect:/board/list";
        }

        // 데이터 처리 실패 시, 게시글 번호를 포함하여 리다이렉트
        return "redirect:/board/update/" + boardFormDto.getId() + "?error=true";
    }


    // 게시글 삭제
    @PostMapping("/delete/{no}")
    public String boardDelete(@PathVariable("no") Integer no) throws Exception {
        // 데이터 요청
        boolean result = boardService.delete(no);
        // 리다이렉트
        // 데이터 처리 성공
        if (result)
            return "redirect:/board/list";
        // 데이터 처리 실패
        return "rediredct:/board/view/" + no + "?error=true";
    }
}



