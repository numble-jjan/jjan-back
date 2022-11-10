package numble.jjan.user.model;

import numble.jjan.util.Role;
import lombok.Data;

@Data
public class LoginRequestModel {

    private String email;
    private String password;
    private Role role;
    private int status;
    private String accessToken;
}
