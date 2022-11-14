package numble.jjan.security.jwt;

import io.swagger.annotations.ApiOperation;
import numble.jjan.user.dto.UserDto;
import numble.jjan.user.entity.User;
import numble.jjan.user.service.UserService;
import numble.jjan.util.AuthenticationUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class RefreshAccessToken {

    private final UserService userService;
    private final AuthenticationUtils utils;
    Environment env;


    @PostMapping("/refreshAccessToken")
    @ApiOperation(value = "refresh 토큰으로 검증 후 access 토큰 재발급")
    private ResponseEntity<?> getRefreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 요청 헤더에서 refresh token 읽기
        String authorizationHeader = request.getHeader("RefreshToken");
        if (authorizationHeader == null || !authorizationHeader.startsWith(env.getProperty("authorization.token.header.prefix"))) {
            log.warn("RefreshToken validation error #1 : refreshToken header isn't exists");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String refreshToken = authorizationHeader.replace(env.getProperty("authorization.token.header.prefix"), "").replaceAll(" ", "");
        byte[] keyBytes = env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8);

        Claims claims;
        String claimsEmail;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(keyBytes)
                    .build()
                    .parseClaimsJws(refreshToken.trim())
                    .getBody();
            claimsEmail = (String) claims.get("email");
            log.info("클레임 이메일 : {}", claimsEmail);
        } catch (MalformedJwtException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid token (malformed)");
            log.warn("Token validation error #2 : Invalid token (malformed)");
            return null;
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token (expired)");
            log.warn("Token validation error #3 : Invalid token (expired)");
            return null;
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid token (bad request)");
            log.warn("Token validation error #4 : Invalid token (request)" + e.getMessage());
            return null;
        }

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        User userEntity = userService.findByEmail(claimsEmail);
        UserDto userDto = new UserDto(userEntity);

        // refresh 토큰 검증
        if (userService.checkValidRefreshToken(claimsEmail, refreshToken)) {
            // refresh 토큰이 검증되면 access 토큰 생성
            Map<String, Object> map = new HashMap<>();
            LocalDateTime ldt = LocalDateTime.now();
            ZonedDateTime now = ldt.atZone(ZoneId.of("Asia/Seoul"));
            long issueTime = now.toInstant().toEpochMilli();
            long access_expiration = issueTime + Long.parseLong(env.getProperty("token.access_expiration_time"));

            String accessToken = utils.makeAccessToken(issueTime, access_expiration, userDto);

            headers.add("AccessToken", accessToken);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
            userService.updateLastRefreshTime(userDto.getEmail());
            return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
