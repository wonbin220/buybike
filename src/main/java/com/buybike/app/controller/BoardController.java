package com.buybike.app.controller;

import com.buybike.app.domain.*;
import com.buybike.app.service.BoardService;
import com.buybike.app.service.CommentService;
import com.github.pagehelper.PageInfo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    @Autowired
    private BoardService boardService;

    private final CommentService commentService;
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
//    @GetMapping("/view/{no}")
//    public String view(@PathVariable("no") Integer no, Model model) throws Exception {
//        // 데이터 요청
//        Board board = boardService.select(no);
//        // 모델 등록
//        model.addAttribute("board", board);
//        return "board/view";
//    }
    @GetMapping("/{no}")
    public String view(@PathVariable("no") Long no, Model model) throws Exception {
        Board board = boardService.select(no);
        model.addAttribute("board", board);

        // 댓글 수 조회 및 모델에 추가
        Long commentCount = commentService.countCommentsByBoardNo(no);
        model.addAttribute("commentCount", commentCount);


        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            currentUsername = userDetails.getUsername();
        } else if (authentication != null) {
            // UserDetails가 아닌 다른 Principal 타입일 경우 (예: String)
            currentUsername = authentication.getName();
        }

        // 현재 사용자가 작성자인지 여부를 boolean 값으로 모델에 추가
        boolean isOwner = board.getMemberId() != null && board.getMemberId().equals(currentUsername);
        model.addAttribute("isOwner", isOwner);



        // --- 디버깅 로그 추가 ---
        log.info("--- 권한 확인 디버깅 ---");
        log.info("게시글 번호: {}", board.getNo());
        log.info("게시글 작성자 ID (board.getMemberId): {}", board.getMemberId());
        log.info("현재 로그인 사용자 ID (currentUsername): {}", currentUsername);
        log.info("authentication: {}", authentication);
        log.info("작성자 여부 (isOwner): {}", isOwner);
        log.info("----------------------");

        return "board/view";
    }


    // 게시글 등록
    @GetMapping("/create")
    public String create(@ModelAttribute(value = "boardFormDto") BoardFormDto boardFormDto) {
        return "board/create";
    }

    @PostMapping(path="/create")
    public String createPostForm(BoardFormDto boardFormDto, HttpSession session) throws Exception {
        // 1. 세션에서 로그인한 회원 정보 가져오기
        MemberFormDto loginInfo = (MemberFormDto) session.getAttribute("userLoginInfo");

        // 로그인하지 않은 경우, 로그인 페이지로 리다이렉트
        if (loginInfo == null) {
            return "redirect:/login";
        }

        // 2. DTO를 Entity로 변환하고 작성자 정보 설정
        Board board = boardFormDto.toEntity();

        board.setMemberId(loginInfo.getMemberId());  // Member 객체의 이름 필드명에 맞게 수정하세요.

        // 3. 서비스 계층에 데이터 저장 요청
        boolean result = boardService.insert(board);

        // 저장 성공 시 목록으로, 실패 시 작성 페이지로 리다이렉트
        if (result) {
            return "redirect:/board/list";
        } else {
            return "redirect:/board/create?error=true";
        }
    }

    // 회원 정보 수정 페이지 출력하기
    @GetMapping("/update/{no}")
    public String update(@PathVariable("no") Long no, Model model) throws Exception {
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
    @DeleteMapping("/delete/{no}")
    @ResponseBody
    public String boardDelete(@PathVariable("no") Long no) throws Exception {
//        // 데이터 요청
//        boolean result = boardService.delete(no);
//        // 리다이렉트
//        // 데이터 처리 성공
//        if (result)
//            return "redirect:/board/list";
//        // 데이터 처리 실패
//        return "redirect:/board/view/" + no + "?error=true";

        try {
            boardService.delete(no);
            return "SUCCESS";
        } catch (Exception e) {
            // 실제로는 로깅을 하는 것이 좋습니다.
            return "FAIL: " + e.getMessage();
        }
    }
}



