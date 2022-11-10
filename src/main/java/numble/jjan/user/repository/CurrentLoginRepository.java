package numble.jjan.user.repository;

import numble.jjan.user.entity.CurrentLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentLoginRepository extends JpaRepository<CurrentLogin, String> {

    void deleteByEmail(String email);
}
