package member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/member/login";
        }
        model.addAttribute("memberId", memberId);
        return "main";
    }
}

