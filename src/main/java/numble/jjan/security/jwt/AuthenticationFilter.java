package numble.jjan.security.jwt;

import numble.jjan.security.auth.PrincipalDetails;
import numble.jjan.user.dto.UserDto;
import numble.jjan.user.model.LoginRequestModel;
import numble.jjan.user.service.UserService;
import numble.jjan.util.AuthenticationUtils;
import numble.jjan.util.Const;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment env;
    private AuthenticationUtils utils;

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager, Environment env, UserService userService, AuthenticationUtils utils) {
        this.userService = userService;
        this.env = env;
        this.utils = utils;
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.warn("================================================ attemptAuthentication");

        try {
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestModel.class);

            System.out.println("creds password = " + creds.getPassword());
            // 	패스워드 정책 체크
            if (!utils.checkPw(creds.getPassword())) {
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "fail 1");
                response.addHeader("message", String.valueOf(Const.LOGIN_FAIL_PASSWORD_POLICY_VIOLATION));
            }

            // 	인증정보 조회
            UserDto userDto = userService.confirmUser(creds.getEmail(), creds.getPassword());


            //	일치하는 정보가 없는 경우
            if (!StringUtils.hasText(userDto.getEmail())) {
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "fail 2");
                response.addHeader("message", String.valueOf(Const.LOGIN_FAIL_NO_MATCHING_ACCOUNT));
                return null;
            }


            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDto.getEmail(),
                    creds.getPassword()
            );
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException ex) {
            log.warn("attemptAuthentication error : {}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String email = principalDetails.getUser().getEmail();

        UserDto userDto = new UserDto(userService.findByEmail(email));

        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime now = ldt.atZone(ZoneId.of("Asia/Seoul"));
        long issueTime = now.toInstant().toEpochMilli();

        long access_expiration = issueTime + Long.parseLong(Objects.requireNonNull(env.getProperty("token.access_expiration_time")));
        long refresh_expiration = issueTime + Long.parseLong(Objects.requireNonNull(env.getProperty("token.refresh_expiration_time")));

        String accessToken = utils.makeAccessToken(issueTime, access_expiration, userDto);
        String refreshToken = utils.makeRefreshToken(issueTime, refresh_expiration, userDto);

        log.warn("================================================ successfulAuthentication userId = " + userDto.getEmail());
        userService.saveLoginInfo(userDto.getEmail(), utils.getClientIP(request), issueTime, refreshToken);
        response.addHeader("message", String.valueOf(Const.LOGIN_SUCCESS));

        // [ Jackson 라이브러리로 json 모양의 String 변환 ]
        // jackson objectmapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();
        // Date 포멧 설정 - 그냥 Jackson을 돌리면 날짜 포멧이 적용 안된 긴 숫자열로 변환된다.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(dateFormat);
        // usersDto의 값을 json 형태로 변환
        String jsonUsersDto = objectMapper.writeValueAsString(userDto);

        response.setContentType("plain/text; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonUsersDto);

        response.setHeader("AccessToken", accessToken);
        response.setHeader("RefreshToken", refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }
}
