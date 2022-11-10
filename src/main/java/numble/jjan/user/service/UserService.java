package numble.jjan.user.service;

import numble.jjan.user.dto.UserDto;
import numble.jjan.user.entity.User;

public interface UserService {

    Long join(User user);

    UserDto confirmUser(String email, String encryptedPassword);

    User findByEmail(String email);

    void saveLoginInfo(String email, String remoteAddr, long issueTime, String refreshToken);

    void deleteExpireToken();

    void deleteToken(String email);

    boolean checkValidRefreshToken(String claimsEmail, String refreshToken);

    void updateLastRefreshTime(String email);
}
