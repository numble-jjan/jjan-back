package numble.jjan.user.controller;

import numble.jjan.user.entity.User;
import numble.jjan.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("join")
    public String join(@RequestBody User user) {
        userService.join(user);
        return "회원 가입 완료";
    }

    @GetMapping("/api/user")
    public String user() {
        return "user";
    }

    @GetMapping("/api/admin")
    public String admin() {
        return "admin";
    }
}
