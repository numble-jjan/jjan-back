package numble.jjan.user.controller;

import io.swagger.annotations.ApiOperation;
import numble.jjan.user.entity.User;
import numble.jjan.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("join")
    @ApiOperation(value = "회원가입")
    public String join(@RequestBody User user) {
        userService.join(user);
        return "회원 가입 완료";
    }

    @GetMapping("/api/user")
    @ApiOperation(value = "일반 유저 테스트용 api")
    public String user() {
        return "user";
    }

    @GetMapping("/api/admin")
    @ApiOperation(value = "관리자 테스트용 api")
    public String admin() {
        return "admin";
    }
}
