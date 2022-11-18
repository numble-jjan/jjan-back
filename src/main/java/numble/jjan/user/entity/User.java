package numble.jjan.user.entity;

import numble.jjan.util.BaseTimeEntity;
import numble.jjan.util.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private int status;

    private String profileImgPath;

    @Builder
    public User(Long id, String email, String password, String name, String phone, String nickName, String address, Role role, int status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.nickName = nickName;
        this.address = address;
        this.role = role;
        this.status = status;
    }


}
