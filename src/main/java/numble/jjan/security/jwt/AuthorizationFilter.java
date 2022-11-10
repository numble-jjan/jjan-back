package numble.jjan.security.jwt;

import numble.jjan.security.auth.PrincipalDetails;
import numble.jjan.user.entity.User;
import numble.jjan.user.repository.UserRepository;
import numble.jjan.util.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final Environment env;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, Environment env) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader(TokenProperties.HEADER_STRING);

        if (jwtHeader == null || !jwtHeader.startsWith(TokenProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = request.getHeader(TokenProperties.HEADER_STRING).replace(TokenProperties.TOKEN_PREFIX, "");
        byte[] keyBytes = env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(jwtToken.trim())
                .getBody();
        String email = (String) claims.get("email");
        System.out.println("email = " + email);

        if (email == null) {
            log.info("인증 실패 필터 HTTP 상태 값 : {}", response.getStatus());
        } else {
            User userEntity = userRepository.findByEmail(email);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("인증 성공 필터 HTTP 상태 값 {}", response.getStatus());

            chain.doFilter(request, response);
        }
    }

}
