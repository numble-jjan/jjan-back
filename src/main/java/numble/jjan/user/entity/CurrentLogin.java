package numble.jjan.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CurrentLogin {

    @Id
    private String email;
    private String refreshToken;
    private String remoteAddr;
    private long expirationTimeLimit;
    private LocalDateTime lastLoginTime;
    private LocalDateTime lastRefreshTime;

    @Builder
    public CurrentLogin(String email, String refreshToken, String remoteAddr, long expirationTimeLimit) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.remoteAddr = remoteAddr;
        this.expirationTimeLimit = expirationTimeLimit;
    }
}
