package numble.jjan.util.controller;

import numble.jjan.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BatchJobController {

    private final UserService userService;

    /**
     * 5분마다 current_login 에서 만료 토큰 로그인 정보 삭제
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void logout() throws Exception {
        userService.deleteExpireToken();
    }
}
