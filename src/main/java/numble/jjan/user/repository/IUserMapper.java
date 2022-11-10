package numble.jjan.user.repository;

import numble.jjan.user.entity.CurrentLogin;
import numble.jjan.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper {

    User confirmUser(String email);

    void deleteExpireToken();

    void saveLoginInfo(CurrentLogin currentLoginEntity);

    void deleteToken(String email);

    boolean checkValidRefreshToken(String claimEmail, String refreshToken);

    void updateLastRefreshTime(String email);
}
