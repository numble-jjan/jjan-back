package numble.jjan.user.dto;

import numble.jjan.user.entity.User;
import numble.jjan.util.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickName;
    private String address;
    private Role role;
    private int status;

    // 엔티티를 Dto로 변환
    public UserDto(User entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        this.name = entity.getName();
        this.phone = entity.getPhone();
        this.nickName = entity.getNickName();
        this.address = entity.getAddress();
        this.role = entity.getRole();
        this.status = entity.getStatus();
    }

    // Dto를 엔티티로 변환
    public User toEntity() {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .nickName(nickName)
                .address(address)
                .role(role)
                .status(status)
                .build();
    }
}
