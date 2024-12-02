package com.lion.demo.controller;

import com.lion.demo.entity.User;
import com.lion.demo.service.UserService;
import com.lion.demo.service.UserServiceImpl;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerForm(){
        return "user/register";
    }

    @PostMapping("/register")
    public String registerProc(String uid, String pwd, String pwd2, String uname, String email){
        if (userService.findByUid(uid) == null && pwd.equals(pwd2) && pwd.length() >= 4){
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt()); // 암호화된 상태
            User user = User.builder()
                    .uid(uid).pwd(hashedPwd).uname(uname).email(email).regDate(LocalDate.now()).role("ROLE_USER")
                    .build();
//             User user = new User(uid, hashedPwd, uname, email, LocalDate.now(), "ROLE_USER"); // 인수를 바꿔서 넣어도 에러가 나지 않음. 검토하기 힘들어서 이런 방식의 초기화는 좋지 않다.
            userService.registerUser(user);
        }
        return "redirect:/user/register";
    }

    @GetMapping("/list")
    public String list(Model model){
        List<User> userList = userService.getUsers();
        model.addAttribute("userList", userList);
        return "user/list";
    }

    @GetMapping("/delete/{uid}")
    public String delete(@PathVariable String uid){
        userService.deleteUser(uid);
        return "redirect:/user/list";
    }

    @GetMapping("/update/{uid}")
    public String updateForm(@PathVariable String uid, Model model){
        User user = userService.findByUid(uid);
        model.addAttribute("user", user);
        return "user/update";
    }
    @PostMapping("/update")
    public String updateProc(String uid, String pwd, String pwd2, String uname, String email, String role){
        User user = userService.findByUid(uid);
        if (pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            user.setPwd(hashedPwd);
        }

        user.setUname(uname);
        user.setEmail(email);
        user.setRole(role);

        userService.updateUser(user);
        return "redirect:/user/list";
    }
}
