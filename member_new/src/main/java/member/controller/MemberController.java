package member.controller;

import member.dto.MemberDTO;
import member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO, Model model) {
        if (memberDTO.getMemberEmail() == null || memberDTO.getMemberEmail().isEmpty() ||
                memberDTO.getMemberPassword() == null || memberDTO.getMemberPassword().isEmpty() ||
                memberDTO.getMemberName() == null || memberDTO.getMemberName().isEmpty() ||
                memberDTO.getMemberDepartment() == null || memberDTO.getMemberDepartment().isEmpty()) {

            model.addAttribute("errorMessage", "모든 필드를 입력해주세요.");
            return "save";
        }if (memberService.isEmailDuplicate(memberDTO.getMemberEmail())) {
            model.addAttribute("errorMessage", "이미 사용 중인 이메일입니다.");
            return "save";
        }

        memberService.save(memberDTO);
        return "login";
    }


    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }
    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model) {

        if (memberDTO.getMemberEmail() == null || memberDTO.getMemberEmail().isEmpty() ||
                memberDTO.getMemberPassword() == null || memberDTO.getMemberPassword().isEmpty()) {

            model.addAttribute("errorMessage", "아이디와 비밀번호를 모두 입력해주세요.");
            return "login";
        }

        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            session.setAttribute("memberId", loginResult.getId());
            model.addAttribute("memberId", loginResult.getId());
            return "main";
        } else {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login";
        }
    }
    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "index";
    }
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @PostMapping("/member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;

    }

}









