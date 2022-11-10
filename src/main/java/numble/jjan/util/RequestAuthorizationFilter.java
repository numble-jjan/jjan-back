package numble.jjan.util;

import numble.jjan.security.service.UriService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@CrossOrigin(origins = "*")
public class RequestAuthorizationFilter extends BasicAuthenticationFilter {

    private UriService uriService;

    private Environment env;

    @Autowired
    public RequestAuthorizationFilter(AuthenticationManager authenticationManager, UriService uriService, Environment env) {
        super(authenticationManager);
        this.uriService = uriService;
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        super.doFilterInternal(request, response, chain);

        //헤더 null 체크
        if (request.getHeader("email") != null
                && request.getHeader("name") != null
                && request.getHeader("address") != null
                && request.getHeader("nickName") != null
                && request.getHeader("role") != null
                && request.getHeader("status") != null) {

            //토큰 파싱
            Claims claims = tokenParser(request);

            //토큰 클레임
            String claimEmail = (String) claims.get("email");
            String claimName = (String) claims.get("name");
            String claimAddress = (String) claims.get("address");
            String claimNickname = (String) claims.get("nickName");
            String claimRole = (String) claims.get("role");
            int claimStatus = (int) claims.get("status");

            //헤더 User 정보
            String email = request.getHeader("email");
            String name = request.getHeader("name");
            String address = request.getHeader("address");
            String nickName = request.getHeader("nickName");
            String role = request.getHeader("role");
            int status = request.getIntHeader("status");

            if ((Objects.equals(role, "ADMIN") || Objects.equals(role, "USER")) && (status != 0)) {

                //토큰 클레임, 헤더 User 정보 일치 여부 확인
                if (email.equals(claimEmail)
                        && name.equals(claimName)
                        && address.equals(claimAddress)
                        && nickName.equals(claimNickname)
                        && role.equals(claimRole)
                        && status == claimStatus) {
                    log.info("토큰과 헤더 정보 일치");
                } else {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "Forged User Information");
                    log.error("Forged User Information");
                    return;
                }
            }

            String uri = request.getRequestURI();
            String method = request.getMethod();

            //uri 체크
            if (uri != null || uri.length() > 1) {
                uri = Utils.makeSecureString(uri);

                if (uriService.checkUriAccess(uri, method, role)) {
                    chain.doFilter(request, response);
                } else {
                    response.sendError(HttpStatus.FORBIDDEN.value(), "role is Not match");
                    log.error("Forbidden request for this role {}", role);
                }
            }
        } else {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "No User info Headers");
            log.error("No User info Headers");
        }
    }

    private Claims tokenParser(HttpServletRequest request) {
        // 토큰 파싱
        String authorizationHeader = request.getHeader(env.getProperty("authorization.token.header.access-name"));
        String token = authorizationHeader.replace(env.getProperty("authorization.token.header.prefix"), "").replaceAll(" ", "");
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token.trim())
                .getBody();
    }
}
