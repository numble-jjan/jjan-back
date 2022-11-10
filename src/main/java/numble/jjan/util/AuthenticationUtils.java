package numble.jjan.util;

import numble.jjan.user.dto.UserDto;
import numble.jjan.user.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AuthenticationUtils {

    String pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]$@$!%*#?&]{6,20}$";
    String pwPattern2 = "(.)\\1\\1\\1";

    private UserService userService;
    private Environment env;


    @Autowired
    public AuthenticationUtils(UserService userService, Environment env) {
        this.userService = userService;
        this.env = env;
    }

    /**
     * 패스워드 정책 체크
     * @param pw
     * @return boolean
     */
    public boolean checkPw(String pw) {
        boolean result = true;
        String message;
        Matcher matcher = Pattern.compile(pwPattern).matcher(pw);
        Matcher matcher2 = Pattern.compile(pwPattern2).matcher(pw);
        System.out.println("checkPw 작동");
        if (!matcher.matches()) {
            System.out.println("!match");
            result = false;
            message = "Not 6-20 characters. ";
            log.warn("/login : !matcher.matches() " + message + " : " + pw);
        } else {
            System.out.println("match!!");
            message = "ok 6-20";
            log.warn("/login ok : matcher.matches() " + message + " : " + pw);
        }

        if (matcher2.find()) {
            result = false;
            message = "Password contains repeating characters";
            log.warn("/login : matcher2.find() " + message + " : " + pw);
        } else {
            message = "ok - repeating";
            log.warn("/login ok : !matcher2.find() " + message + " : " + pw);
        }
        return result;
    }


    /**
     * 접근 IP 체크
     * @param request
     * @return
     */
    public String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        log.info(">>>> X-FORWARDED-FOR : " + ip);

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info(">>>> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
            log.info(">>>> WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info(">>>> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info(">>>> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        log.info(">>>> Result : IP Address : " + ip);

        return ip;
    }

    /**
     * Access 토큰 생성
     * @param issueTime
     * @param access_expiration
     * @param userDto
     * @return
     */
    public String makeAccessToken(long issueTime, long access_expiration, UserDto userDto) {

        return Jwts.builder()
                .setSubject("accessToken")
                .claim("email", userDto.getEmail())
                .claim("name", userDto.getName())
                .claim("address", userDto.getAddress())
                .claim("nickName", userDto.getNickName())
                .claim("role", userDto.getRole())
                .claim("status", userDto.getStatus())
                .setExpiration(Date.from(Instant.ofEpochMilli(access_expiration)))
                .setIssuedAt(Date.from(Instant.ofEpochMilli(issueTime)))
                .signWith(Keys.hmacShaKeyFor(Objects.requireNonNull(env.getProperty("token.secret")).getBytes(StandardCharsets.UTF_8))).compact();
    }

    /**
     * refresh 토큰 생성
     * @param issueTime
     * @param refresh_expiration
     * @param userDto
     * @return
     */
    public String makeRefreshToken(long issueTime, long refresh_expiration, UserDto userDto) {

        return Jwts.builder()
                .setSubject("refreshToken")
                .claim("email", userDto.getEmail())
                .setExpiration(Date.from(Instant.ofEpochMilli(refresh_expiration)))
                .setIssuedAt(Date.from(Instant.ofEpochMilli(issueTime)))
                .signWith(Keys.hmacShaKeyFor(Objects.requireNonNull(env.getProperty("token.secret")).getBytes(StandardCharsets.UTF_8))).compact();
    }
}
