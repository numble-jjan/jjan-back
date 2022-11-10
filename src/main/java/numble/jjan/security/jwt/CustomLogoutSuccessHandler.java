package numble.jjan.security.jwt;

import numble.jjan.user.service.UserService;
import numble.jjan.util.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final Environment env;
    private final UserService userService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String token;
        Claims claims;
        byte[] keyBytes = env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8);
        String email;

        // 에러 발생해도 로그아웃은 정상 실행된 것처럼 처리하기. 원래 JWT 로그아웃은 Redis 블랙리스트로 많이 처리하더라. 나중에 바꾸는게 좋을 듯
        try {
            token = request.getHeader(TokenProperties.HEADER_STRING).replace(TokenProperties.TOKEN_PREFIX, "");

            claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                    .build()
                    .parseClaimsJws(token.trim())
                    .getBody();
            email = (String) claims.get("email");
            log.info("로그아웃 호출 : {}", email);

            // 토큰 삭제
            userService.deleteToken(email);

        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }
}
